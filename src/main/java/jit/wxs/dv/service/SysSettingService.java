package jit.wxs.dv.service;


import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.SysSetting;
import jit.wxs.dv.domain.vo.ResultVO;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
public interface SysSettingService extends IService<SysSetting> {
    /**
     * 设置资源信息
     * @param resContent 内容资源
     * @param resThumbnail 缩略图资源
     * @param contentIp 内容资源IP
     * @param thumbnailIp 缩略图资源IP
     * @author jitwxs
     * @since 2018/10/4 1:41
     */
    ResultVO setResInfo(String resContent, String resThumbnail, String contentIp, String thumbnailIp);

    /**
     * 获取资源信息
     * @author jitwxs
     * @since 2018/10/4 1:41
     */
    ResultVO<Map> getResInfo();

    /**
     * 获取视频和图片数目
     * @author jitwxs
     * @since 2018/10/4 1:42
     */
    Map<String, String> getVideoAndPictureNum();

    void updateVideoAndPictureNum();

    String getResContent();

    String getResThumbnail();

    String getContentIp();

    String getThumbnailIp();

    void insertOrUpdate(String key, String value);

    ResultVO getCarouselInfo();

    /**
     * 设置缩略图信息
     * @param json json信息
     * @author jitwxs
     * @since 2018/10/6 22:55
     */
    ResultVO setCarouselInfo(String json);
}
