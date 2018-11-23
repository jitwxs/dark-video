package jit.wxs.dv.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.ThumbnailService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * 缩略图管理
 * @author jitwxs
 * @since 2018/10/4 2:17
 */
@Api(tags = {"缩略图管理"})
@RestController
@RequestMapping("/thumbnail")
public class ThumbnailController {
    @Autowired
    private ThumbnailService thumbnailService;

    @ApiOperation(value= "清理缩略图", notes = "基于WebSocket，限制管理员访问")
    @DeleteMapping("/clean")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO cleanThumbnail(@ApiIgnore HttpSession session) {
        thumbnailService.cleanThumbnailTask(session.getId());

        return ResultVOUtils.successWithMsg("开始清理缩略图");
    }
}
