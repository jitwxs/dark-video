package jit.wxs.dv.controller;

import jit.wxs.dv.convert.ContentBOConvert;
import jit.wxs.dv.domain.entity.DvContentAffix;
import jit.wxs.dv.domain.enums.CategoryLevelEnum;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvContentAffixService;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 内容Controller
 * @author jitwxs
 * @since 2018/10/4 14:59
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private DvContentService contentService;
    @Autowired
    private DvContentAffixService contentAffixService;
    @Autowired
    private ContentBOConvert contentBOConvert;

    /**
     * 获取指定目录下最新内容
     * @param category 目录ID
     * @param level 目录级别
     * @param count 内容条数
     * @author jitwxs
     * @since 2018/10/4 15:00
     */
    @GetMapping("/{category}/latest")
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
    public ResultVO listHotContent(@PathVariable String category,
                                   Integer level,
                                   @RequestParam(defaultValue = "8") Integer count) {
        CategoryLevelEnum levelEnum;
        if(StringUtils.isBlank(category) || (levelEnum = CategoryLevelEnum.getEnum(level)) == null) {
            return ResultVOUtils.error(ResultEnum.PARAM_ERROR);
        }

        return contentService.listHotContent(levelEnum, category, count);
    }

    /**
     * 读取附件信息
     * @author jitwxs
     * @since 2018/10/5 21:26
     */
    @GetMapping("/suffix/{suffix}")
    public ResultVO getContentSuffix(@PathVariable String suffix) {
        DvContentAffix contentAffix = contentAffixService.selectById(suffix);

        if("video".equals(contentAffix.getType())) {
            return ResultVOUtils.success(contentBOConvert.convertAffix2Video(contentAffix, null, null));
        } else {
            return ResultVOUtils.success(contentBOConvert.convertAffix2BO(contentAffix, null, null));
        }
    }
}
