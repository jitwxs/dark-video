package jit.wxs.dv.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * 用户相关Controller
 * @author jitwxs
 * @since 2018/10/14 18:41
 */
@Api(tags = {"用户管理"})
@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserService userService;

    @ApiOperation(value= "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name="rawPassword", value="原始密码", required = true),
            @ApiImplicitParam(paramType = "query", name="newPassword", value="新密码", required = true)
    })
    @PostMapping(value = "/password", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO resetPassword(@NotBlank(message = "原始密码不能为空") String rawPassword,
                                  @NotBlank(message = "新密码不能为空") String newPassword) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        return userService.resetPassword(username, rawPassword, newPassword);
    }
}
