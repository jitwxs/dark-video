package jit.wxs.dv.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 设置Controller
 * @author jitwxs
 * @since 2018-10-03
 */
@Api(tags = {"设置管理"})
@RestController
@RequestMapping("/setting")
public class SettingController {
    @Autowired
    private SysSettingService sysSettingService;

    @ApiOperation(value = "设置资源配置", notes = "限制管理员访问")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name="resContent", value="内容根目录", required = true),
            @ApiImplicitParam(paramType = "form", name="resThumbnail", value="缩略图根目录", required = true),
            @ApiImplicitParam(paramType = "form", name="contentIp", value="内容资源服务器IP", required = true),
            @ApiImplicitParam(paramType = "form", name="thumbnailIp", value="缩略图资源服务器IP", required = true)
    })
    @PostMapping(value = "/res", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO setResInfo(String resContent, String resThumbnail, String contentIp, String thumbnailIp) {
        if(StringUtils.isBlank(resContent, resThumbnail, contentIp, thumbnailIp)) {
            return ResultVOUtils.error(ResultEnum.PARAM_ERROR);
        }
        return sysSettingService.setResInfo(resContent, resThumbnail, contentIp, thumbnailIp);
    }

    @ApiOperation(value = "获取资源配置", notes = "限制管理员访问")
    @GetMapping(value = "/res", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO<Map> getResInfo() {
        return sysSettingService.getResInfo();
    }

    @ApiOperation(value = "获取缩略图信息")
    @GetMapping(value = "/carousel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO getCarouselInfo() {
        return sysSettingService.getCarouselInfo();
    }

    @ApiOperation(value = "设置缩略图信息", notes = "限制管理员访问")
    @PostMapping(value = "/carousel", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO setCarouselInfo(String json) {
        return sysSettingService.setCarouselInfo(json);
    }

    @ApiOperation(value = "获取视频、图片数量")
    @GetMapping(value = "/vpn", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO<Map> getVideoAndPictureNum() {
        Map<String, String> map = sysSettingService.getVideoAndPictureNum();

        return ResultVOUtils.success(map);
    }
}

