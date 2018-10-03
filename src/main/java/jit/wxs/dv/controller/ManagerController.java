package jit.wxs.dv.controller;

import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.SysSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 管理中心Controller
 * @author jitwxs
 * @since 2018/10/4 1:39
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private SysSettingService sysSettingService;

    /**
     * 设置资源配置
     * @param resRoot 资源根目录
     * @param resIP 资源服务器IP
     * @author jitwxs
     * @since 2018/6/17 12:18
     */
    @PostMapping("/res")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO setResInfo(@NotNull String resRoot, @NotNull String resIP) {
        return sysSettingService.setResInfo(resRoot, resIP);
    }

    /**
     * 获取资源配置
     * @author jitwxs
     * @since 2018/6/17 12:26
     */
    @GetMapping("/res")
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
    public ResultVO getVideoAndPictureNum() {
        return sysSettingService.getVideoAndPictureNum();
    }
}
