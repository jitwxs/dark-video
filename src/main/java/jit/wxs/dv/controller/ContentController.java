package jit.wxs.dv.controller;

import jit.wxs.dv.domain.dto.ContentDTO;
import jit.wxs.dv.domain.entity.DvContentAffix;
import jit.wxs.dv.domain.enums.CategoryLevelEnum;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvContentAffixService;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 内容Controller
 * @author jitwxs
 * @since 2018/10/4 14:59
 */
@Controller
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private DvContentService contentService;
    @Autowired
    private DvContentAffixService contentAffixService;

    /**
     * 内容页面
     * @author jitwxs
     * @since 2018/10/5 10:42
     */
    @RequestMapping("/{contentId}")
    public String showContent(@PathVariable String contentId, ModelMap map,
                              HttpServletRequest request, HttpServletResponse response) {
        // 判断合法
        ContentDTO contentDTO = null;
        if(StringUtils.isBlank(contentId) || (contentDTO = contentService.getContentDTO(contentId)) == null) {
            try {
                request.setAttribute("ERR_MSG", ResultEnum.CONTENT_NOT_EXIST);
                request.getRequestDispatcher("/auth/error").forward(request,response);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 1、读取内容数据
        map.put("content", contentDTO);
        map.put("type", contentDTO.getType());
        // 2、如果内容为dir，读取附件数据
        if("dir".equals(contentDTO.getType())) {
            List<DvContentAffix> contentAffixes = contentAffixService.listByContentId(contentId);
            map.put("affixes", contentAffixes);
        }

        return "content";
    }

    /**
     * 获取指定目录下最新内容
     * @param category 目录ID
     * @param level 目录级别
     * @param count 内容条数
     * @author jitwxs
     * @since 2018/10/4 15:00
     */
    @GetMapping("/{category}/latest")
    @ResponseBody
    public ResultVO listLatestContent(@PathVariable String category,
                                      Integer level,
                                      @RequestParam(defaultValue = "8") Integer count) {
        CategoryLevelEnum levelEnum;
        if(StringUtils.isBlank(category) || (levelEnum = CategoryLevelEnum.getEnum(level)) == null) {
            return ResultVOUtils.error(ResultEnum.PARAM_ERROR);
        }

        return contentService.listLatestContent(levelEnum, category, count);
    }

    /**
     * 获取指定目录下最热内容
     * @param category 目录ID
     * @param level 目录级别
     * @param count 内容条数
     * @author jitwxs
     * @since 2018/10/4 15:00
     */
    @GetMapping("/{category}/hot")
    @ResponseBody
    public ResultVO listHotContent(@PathVariable String category,
                                   Integer level,
                                   @RequestParam(defaultValue = "8") Integer count) {
        CategoryLevelEnum levelEnum;
        if(StringUtils.isBlank(category) || (levelEnum = CategoryLevelEnum.getEnum(level)) == null) {
            return ResultVOUtils.error(ResultEnum.PARAM_ERROR);
        }

        return contentService.listHotContent(levelEnum, category, count);
    }
}
