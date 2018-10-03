package jit.wxs.dv.security;


import jit.wxs.dv.domain.entity.SysLogin;
import jit.wxs.dv.domain.enums.RoleEnum;
import jit.wxs.dv.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author jitwxs
 * @date 2018/3/30 9:17
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private SysLoginService loginService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SysLogin sysLogin = loginService.selectById(s);

        // 判断用户是否合法
        if(sysLogin == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        RoleEnum roleEnum = RoleEnum.getEnum(sysLogin.getRoleId());
        if(roleEnum == null) {
            throw new UsernameNotFoundException("用户权限错误");
        }

        authorities.add(new SimpleGrantedAuthority(roleEnum.getMessage()));

        // 返回UserDetails实现类
        return new User(sysLogin.getUsername(), sysLogin.getPassword(), authorities);
    }
}
