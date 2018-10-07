package jit.wxs.dv.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import jit.wxs.dv.convert.ContentBOConvert;
import jit.wxs.dv.convert.ContentVOConvert;
import jit.wxs.dv.domain.bo.ContentBO;
import jit.wxs.dv.domain.bo.PictureBO;
import jit.wxs.dv.domain.bo.VideoBO;
import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.domain.entity.DvContentAffix;
import jit.wxs.dv.domain.entity.DvContentComment;
import jit.wxs.dv.domain.enums.CategoryLevelEnum;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ContentVO;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.domain.vo.TreeVO;
import jit.wxs.dv.schedule.CachePool;
import jit.wxs.dv.service.DvCategoryService;
import jit.wxs.dv.service.DvContentAffixService;
import jit.wxs.dv.service.DvContentCommentService;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
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
    private DvCategoryService categoryService;
    @Autowired
    private DvContentService contentService;
    @Autowired
    private DvContentAffixService contentAffixService;
    @Autowired
    private DvContentCommentService contentCommentService;
    @Autowired
    private ContentBOConvert contentBOConvert;
    @Autowired
    private ContentVOConvert contentVOConvert;

    /**
     * 内容页面
     * @author jitwxs
     * @since 2018/10/5 10:42
     */
    @RequestMapping("/{contentId}")
    public String showContent(@PathVariable String contentId, ModelMap map,
                              HttpServletRequest request, HttpServletResponse response) {
        // 判断合法
        DvContent content = null;
        if(StringUtils.isBlank(contentId) || (content = contentService.selectById(contentId)) == null) {
            try {
                request.setAttribute("ERR_MSG", ResultEnum.CONTENT_NOT_EXIST);
                request.getRequestDispatcher("/auth/error").forward(request,response);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 记录点击数
        CachePool cachePool = CachePool.getInstance();
        if(cachePool.getCacheItem(contentId) != null) {
            cachePool.putCacheItem(contentId, (int)cachePool.getCacheItem(contentId) + 1);
        } else {
            cachePool.putCacheItem(contentId, 1);
        }

        map.put("content", contentVOConvert.convert(content));
        map.put("type", content.getType());

        List<VideoBO> videoBOS = new LinkedList<>();
        List<PictureBO> pictureBOS = new LinkedList<>();
        List<ContentBO> contentBOS = new LinkedList<>();
        // 判断类型
        switch (content.getType()) {
            case "video":
                VideoBO videoBO = contentBOConvert.convert2Video(content);
                videoBOS.add(videoBO);
                map.put("videoBO", videoBOS);
                break;
            case "picture":
                PictureBO pictureBO = contentBOConvert.convert2Picture(content);
                pictureBOS.add(pictureBO);
                map.put("pictureBO", pictureBOS);
            case "music":
            case "page":
                ContentBO contentBO = contentBOConvert.convert2BO(content);
                contentBOS.add(contentBO);
                map.put("contentBO",contentBOS);
                break;
            case "dir":
                List<DvContentAffix> videoAffixes =
                        contentAffixService.selectList(new EntityWrapper<DvContentAffix>()
                                .eq("content_id", contentId)
                                .eq("type", "video")
                                .orderBy("name", true));
                List<DvContentAffix> pictureAffixes =
                        contentAffixService.selectList(new EntityWrapper<DvContentAffix>()
                                .eq("content_id", contentId)
                                .eq("type", "picture")
                                .orderBy("name", true));
                List<DvContentAffix> otherAffixes =
                        contentAffixService.selectList(new EntityWrapper<DvContentAffix>()
                                .eq("content_id", contentId)
                                .ne("type", "video")
                                .orderBy("name", true));

                videoBOS = contentBOConvert.convertAffix2Video(videoAffixes, content.getCreateDate(), content.getAuthor());
                pictureBOS = contentBOConvert.convertAffix2Picture(pictureAffixes);
                contentBOS = contentBOConvert.convertAffix2BO(otherAffixes, content.getCreateDate(), content.getAuthor());
                map.put("videoBO", videoBOS);
                map.put("pictureBO", pictureBOS);
                map.put("contentBO",contentBOS);
                break;
            default:
                break;
        }

        // 读取评论
        List<DvContentComment> comments = contentCommentService.listByContentId(contentId);
        map.put("comments",comments);
        map.put("commentCount",comments.size());

        // 读取推荐视频
        List<ContentVO> recommends = contentService.listRecommend(content, 10);
        map.put("recommends", recommends);

        // 获取类别导航头
        List<TreeVO> navCategory = categoryService.listNavCategory();
        map.addAttribute("navCategory", navCategory);

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

    /**
     * 读取附件信息
     * @author jitwxs
     * @since 2018/10/5 21:26
     */
    @GetMapping("/suffix/{suffix}")
    @ResponseBody
    public ResultVO getContentSuffix(@PathVariable String suffix) {
        DvContentAffix contentAffix = contentAffixService.selectById(suffix);

        if("video".equals(contentAffix.getType())) {
            return ResultVOUtils.success(contentBOConvert.convertAffix2Video(contentAffix, null, null));
        } else {
            return ResultVOUtils.success(contentBOConvert.convertAffix2BO(contentAffix, null, null));
        }
    }
}
