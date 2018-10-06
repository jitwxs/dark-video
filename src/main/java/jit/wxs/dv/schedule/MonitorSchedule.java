package jit.wxs.dv.schedule;

import jit.wxs.dv.service.SysSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author jitwxs
 * @since 2018/8/25 11:50
 */
@Slf4j
@Component
public class MonitorSchedule {
    @Autowired
    private SysSettingService settingService;

    /**
     * 更新视频和图片数目
     * 每隔30分钟更新
     * @author jitwxs
     * @since 2018/7/9 13:44
     */
    @Scheduled(fixedDelay = 1800_000)
    public void updateVideoAndPictureNum() {
        LocalDateTime time = LocalDateTime.now();

        settingService.updateVideoAndPictureNum();

        log.info("【{}】执行定时任务：视频、图片数目更新", time);
    }
}