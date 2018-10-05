package jit.wxs.dv.service;

import jit.wxs.dv.domain.vo.ResultVO;

/**
 * @author jitwxs
 * @since 2018/10/4 2:18
 */
public interface ThumbnailService {
    /**
     * 清理缩略图
     * @author jitwxs
     * @since 2018/10/4 2:19
     */
    ResultVO cleanThumbnail();

    /**
     * 缩略图path --> url
     * @author jitwxs
     * @since 2018/10/5 11:25
     */
    String getUrl(String path);
}
