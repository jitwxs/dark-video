package jit.wxs.dv.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.dv.convert.ContentBOConvert;
import jit.wxs.dv.convert.ContentVOConvert;
import jit.wxs.dv.domain.bo.ContentBO;
import jit.wxs.dv.domain.bo.PictureBO;
import jit.wxs.dv.domain.bo.VideoBO;
import jit.wxs.dv.domain.entity.*;
import jit.wxs.dv.domain.enums.CategoryLevelEnum;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.enums.RoleEnum;
import jit.wxs.dv.domain.vo.ContentVO;
import jit.wxs.dv.domain.vo.TreeVO;
import jit.wxs.dv.schedule.CachePool;
import jit.wxs.dv.service.*;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 页面展示Controller
 * @author jitwxs
 * @since 2018/10/7 22:12
 */
@Controller
public class PageController {
    @Autowired
    private DvCategoryService categoryService;
    @Autowired
    private DvContentService contentService;
    @Autowired
    private DvContentAffixService contentAffixService;
    @Autowired
    private DvContentCommentService contentCommentService;
    @Autowired
    private DvUserLookLaterService lookLaterService;
    @Autowired
    private ESService esService;
    @Autowired
    private ContentVOConvert contentVOConvert;
    @Autowired
    private ContentBOConvert contentBOConvert;

    /**
     * 动态页面
     * @author jitwxs
     * @since 2018/10/7 21:19
     */
    @RequestMapping("/follow")
    public String showFollow(@RequestParam(defaultValue = "1") Integer current,
                             @RequestParam(defaultValue = "10") Integer size,
                             ModelMap map) {
        Page<DvContent> selectPage = contentService.selectPage(new Page<>(current, size, "create_date", false));
        Page<ContentVO> page1 = new Page<>();
        BeanUtils.copyProperties(selectPage, page1);
        page1.setRecords(contentVOConvert.convert(selectPage.getRecords()));

        map.addAttribute("page", page1);
        return "follow";
    }

    /**
     * 稍后再看页面
     * @author jitwxs
     * @since 2018/10/7 22:19
     */
    @RequestMapping("/lookLater")
    public String showLookLater(@RequestParam(defaultValue = "1") Integer current,
                             @RequestParam(defaultValue = "10") Integer size,
                             ModelMap map) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Page<DvUserLookLater> page = lookLaterService.selectPage(
                new Page<>(current, size, "create_date", false),
                new EntityWrapper<DvUserLookLater>().eq("username", username));


        List<DvUserLookLater> records = page.getRecords();
        for(DvUserLookLater lookLater : records) {
            DvContent content = contentService.selectById(lookLater.getContentId());
            ContentVO contentVO = contentVOConvert.convert(content);
            lookLater.setContentVO(contentVO);
        }


        map.addAttribute("page", page);
        return "lookLater";
    }

    /**
     * 管理中心页面
     * @author jitwxs
     * @since 2018/10/4 1:26
     */
    @RequestMapping("/manager")
    public String showManager() {
        Iterator<? extends GrantedAuthority> iterator = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator();
        while (iterator.hasNext()) {
            String authority = iterator.next().getAuthority();
            if (RoleEnum.ROLE_USER.getMessage().equals(authority) || RoleEnum.ROLE_VIP.getMessage().equals(authority)) {
                return "user/manager";
            } else if(RoleEnum.ROLE_ADMIN.getMessage().equals(authority)) {
                return "admin/manager";
            }
        }
        return "login";
    }

    /**
     * 类别页面
     * @author jitwxs
     * @since 2018/10/4 21:23
     */
    @RequestMapping("/category/{categoryId}")
    public String showCategory(@PathVariable String categoryId,
                               @RequestParam(defaultValue = "1") Integer current,
                               @RequestParam(defaultValue = "16") Integer size,
                               @RequestParam(defaultValue = "create_date") String sort,
                               @RequestParam(defaultValue = "true") Boolean isAsc,
                               ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        DvCategory category = categoryService.selectById(categoryId);

        if(StringUtils.isBlank(categoryId) || category == null) {
            try {
                request.setAttribute("ERR_MSG", ResultEnum.CATEGORY_NOT_EXIST);
                request.getRequestDispatcher("/auth/error").forward(request,response);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 获取目录级别
        CategoryLevelEnum levelEnum = category.getParentId() == null ? CategoryLevelEnum.FIRST : CategoryLevelEnum.SECOND;
        // 读取内容
        Page<ContentVO> contentPage = contentService.pageByCategory(levelEnum, categoryId, new Page<>(current, size, sort, isAsc));

        // 1级目录ID
        String firstCategoryId;

        // 读取该分类所属一级分类下所有二级分类列表
        List<DvCategory> secondCategories;
        if(category.getParentId() == null) {
            secondCategories = categoryService.listCategory(categoryId);
            firstCategoryId = categoryId;
        } else {
            secondCategories = categoryService.listCategory(category.getParentId());
            firstCategoryId = category.getParentId();
        }

        // 获取类别导航头
        List<TreeVO> navCategory = categoryService.listNavCategory();

        map.addAttribute("firstCategoryId", firstCategoryId);
        map.addAttribute("navCategory", navCategory);
        map.addAttribute("category", category);
        map.addAttribute("contents", contentPage);
        map.addAttribute("secondCategories", secondCategories);

        return "category";
    }

    /**
     * 内容页面
     * @author jitwxs
     * @since 2018/10/5 10:42
     */
    @RequestMapping("/content/{contentId}")
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
     * 搜索内容
     * @author jitwxs
     * @since 2018/10/10 0:19
     */
    @RequestMapping("/content/search")
    public String search(String keyword, ModelMap map,
                           @RequestParam(defaultValue = "1") Integer current,
                           @RequestParam(defaultValue = "10") Integer size) {

        Page<ESContent> page = esService.searchContent(keyword, current, size);

        map.addAttribute("keyword", keyword);
        map.addAttribute("page", page);

        return "search";
    }
}
