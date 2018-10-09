package jit.wxs.dv.controller;

import jit.wxs.dv.domain.entity.DvCategory;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvCategoryService;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 目录Controller
 * @author jitwxs
 * @since 2018/10/4 2:25
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private DvCategoryService categoryService;
    @Autowired
    private SysSettingService settingService;

    /**
     * 生成目录
     * @author jitwxs
     * @since 2018/10/4 2:26
     */
    @PostMapping("/gen")
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
    public ResultVO listCategory(String parentId) {
        List<DvCategory> categories = categoryService.listCategory(parentId);

        return ResultVOUtils.success(categories);
    }
}
