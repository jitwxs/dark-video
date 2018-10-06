package jit.wxs.dv.controller;

import jit.wxs.dv.domain.enums.RoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Iterator;

/**
 * 管理中心Controller
 * @author jitwxs
 * @since 2018/10/4 1:39
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {
    /**
     * 管理中心页面
     * @author jitwxs
     * @since 2018/10/4 1:26
     */
    @RequestMapping("")
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
}
