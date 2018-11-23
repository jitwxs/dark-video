package jit.wxs.dv.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jit.wxs.dv.domain.entity.DvCategory;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvCategoryService;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 分类Controller
 * @author jitwxs
 * @since 2018/10/15 13:23
 */
@Api(tags = {"分类管理"})
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private DvCategoryService categoryService;
    @Autowired
    private SysSettingService settingService;

    @ApiOperation(value= "生成分类", notes = "基于WebSocket")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/gen", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO genCategory(@ApiIgnore HttpSession session) {
        String resContent = settingService.getResContent();
        if(resContent == null) {
            return ResultVOUtils.error(ResultEnum.RES_ROOT_ERROR);
        }

        String author = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        categoryService.genCategory(resContent, author, session.getId());

        return ResultVOUtils.successWithMsg("开始生成目录");
    }

    @ApiOperation(value= "获取分类列表")
    @ApiImplicitParam(paramType = "query", name="parentId", value="父目录ID，可为NULL")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO<List<DvCategory>> listCategory(String parentId) {
        List<DvCategory> categories = categoryService.listCategory(parentId);

        return ResultVOUtils.success(categories);
    }
}
