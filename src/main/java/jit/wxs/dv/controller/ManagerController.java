package jit.wxs.dv.controller;

import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.enums.RoleEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;

/**
 * 管理中心Controller
 * @author jitwxs
 * @since 2018/10/4 1:39
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private SysSettingService sysSettingService;

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

    /**
     * 设置资源配置
     * @param resContent 内容根目录
     * @param resThumbnail 缩略图根目录
     * @param contentIp 内容资源服务器IP
     * @param thumbnailIp 缩略图资源服务器IP
     * @author jitwxs
     * @since 2018/6/17 12:18
     */
    @PostMapping("/res")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO setResInfo(String resContent, String resThumbnail, String contentIp, String thumbnailIp) {
        if(StringUtils.isBlank(resContent, resThumbnail, contentIp, thumbnailIp)) {
            return ResultVOUtils.error(ResultEnum.PARAM_ERROR);
        }
        return sysSettingService.setResInfo(resContent, resThumbnail, contentIp, thumbnailIp);
    }

    /**
     * 获取资源配置
     * @author jitwxs
     * @since 2018/6/17 12:26
     */
    @GetMapping("/res")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO getResInfo() {
        return sysSettingService.getResInfo();
    }

    /**
     * 获取视频、图片数量
     * vpn : video picture num
     * @author jitwxs
     * @since 2018/8/25 12:05
     */
    @GetMapping("/vpn")
    @ResponseBody
    public ResultVO getVideoAndPictureNum() {
        return sysSettingService.getVideoAndPictureNum();
    }
}
