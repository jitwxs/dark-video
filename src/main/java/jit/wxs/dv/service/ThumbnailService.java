package jit.wxs.dv.service;

/**
 * @author jitwxs
 * @since 2018/10/4 2:18
 */
public interface ThumbnailService {
    /**
     * 清理缩略图任务
     * @author jitwxs
     * @since 2018/10/4 2:19
     */
    void cleanThumbnailTask(String sessionId);

    /**
     * 缩略图path --> url
     * @author jitwxs
     * @since 2018/10/5 11:25
     */
    String getUrl(String path);
}
