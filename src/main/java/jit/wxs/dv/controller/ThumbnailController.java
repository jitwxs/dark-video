package jit.wxs.dv.controller;

import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.ThumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 缩略图管理
 * @author jitwxs
 * @since 2018/10/4 2:17
 */
@RestController
@RequestMapping("/thumbnail")
public class ThumbnailController {
    @Autowired
    private ThumbnailService thumbnailService;

    /**
     * 清理缩略图
     * @author jitwxs
     * @since 2018/10/4 2:18
     */
    @DeleteMapping("/clean")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultVO cleanThumbnail() {
        return thumbnailService.cleanThumbnail();
    }

}
