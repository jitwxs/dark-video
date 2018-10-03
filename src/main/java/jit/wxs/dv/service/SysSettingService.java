package jit.wxs.dv.service;


import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.SysSetting;
import jit.wxs.dv.domain.vo.ResultVO;

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
     * @author jitwxs
     * @since 2018/10/4 1:41
     */
    ResultVO setResInfo(String resRoot, String resIP);

    /**
     * 获取资源信息
     * @author jitwxs
     * @since 2018/10/4 1:41
     */
    ResultVO getResInfo();

    /**
     * 获取视频和图片数目
     * @author jitwxs
     * @since 2018/10/4 1:42
     */
    ResultVO getVideoAndPictureNum();

    void updateVideoAndPictureNum();

    String getResRoot();

    String getResIp();

    void insertOrUpdate(String key, String value);
}
