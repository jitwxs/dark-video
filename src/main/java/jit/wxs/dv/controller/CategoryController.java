package jit.wxs.dv.controller;

import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.dv.domain.entity.DvCategory;
import jit.wxs.dv.domain.enums.CategoryLevelEnum;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ContentVO;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.domain.vo.TreeVO;
import jit.wxs.dv.service.DvCategoryService;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 目录Controller
 * @author jitwxs
 * @since 2018/10/4 2:25
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private DvCategoryService categoryService;
    @Autowired
    private DvContentService contentService;
    @Autowired
    private SysSettingService settingService;

    /**
     * 类别页面
     * @author jitwxs
     * @since 2018/10/4 21:23
     */
    @RequestMapping("/{categoryId}")
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
     * 生成目录
     * @author jitwxs
     * @since 2018/10/4 2:26
     */
    @PostMapping("/gen")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO genCategory() {
        String resContent = settingService.getResContent();
        if(resContent == null) {
            return ResultVOUtils.error(ResultEnum.RES_ROOT_ERROR);
        }

        return categoryService.genCategory(resContent);
    }

    /**
     * 获取目录
     * @param parentId 父目录ID，为空获取顶级目录
     * @author jitwxs
     * @since 2018/10/4 13:52
     */
    @GetMapping("/list")
    @ResponseBody
    public ResultVO listCategory(String parentId) {
        List<DvCategory> categories = categoryService.listCategory(parentId);

        return ResultVOUtils.success(categories);
    }
}
