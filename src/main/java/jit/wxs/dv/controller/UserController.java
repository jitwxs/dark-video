package jit.wxs.dv.controller;

import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserService userService;
    /**
     * 修改密码
     * @author jitwxs
     * @since 2018/10/14 18:42
     */
    @PostMapping("/password")
    public ResultVO resetPassword(@NotBlank(message = "原始密码不能为空") String rawPassword,
                                  @NotBlank(message = "新密码不能为空") String newPassword) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        return userService.resetPassword(username, rawPassword, newPassword);
    }
}
