package jit.wxs.dv.controller;


import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-03
 */
@RestController
@RequestMapping("/setting")
public class SettingController {
    @Autowired
    private SysSettingService sysSettingService;

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO getResInfo() {
        return sysSettingService.getResInfo();
    }

    /**
     * 获取缩略图信息
     * @author jitwxs
     * @since 2018/10/6 22:53
     */
    @GetMapping("/carousel")
    public ResultVO getCarouselInfo() {
        return sysSettingService.getCarouselInfo();
    }

    /**
     * 设置缩略图信息
     * @author jitwxs
     * @since 2018/10/6 22:54
     */
    @PostMapping("/carousel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO setCarouselInfo( String json) {
        return sysSettingService.setCarouselInfo(json);
    }

    /**
     * 获取视频、图片数量
     * vpn : video picture num
     * @author jitwxs
     * @since 2018/8/25 12:05
     */
    @GetMapping("/vpn")
    public ResultVO getVideoAndPictureNum() {
        Map<String, String> map = sysSettingService.getVideoAndPictureNum();

        return ResultVOUtils.success(map);
    }
}

