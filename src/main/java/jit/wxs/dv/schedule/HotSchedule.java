package jit.wxs.dv.schedule;

import jit.wxs.dv.service.DvContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
public class HotSchedule {
    @Autowired
    private DvContentService contentService;

    /**
     * 每隔30分钟更新
     * @author jitwxs
     * @since 2018/7/9 13:44
     */
    @Scheduled(initialDelay=1800_000, fixedDelay = 600_000)
    public void updateHot() {
        CachePool cachePool = CachePool.getInstance();
        Set<String> strings = cachePool.listKeys();

        for(String contentId : strings) {
            contentService.setHot(contentId, (int) cachePool.getCacheItem(contentId));
            cachePool.removeCacheItem(contentId);
        }

        log.info("【{}】执行定时任务：更新热度值", LocalDateTime.now());
    }
}
