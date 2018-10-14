package jit.wxs.dv.service.impl;

import jit.wxs.dv.mapper.DvContentMapper;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.service.ThumbnailService;
import jit.wxs.dv.util.FileUtils;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collection;

/**
 * @author jitwxs
 * @since 2018/10/4 2:19
 */
@Service
public class ThumbnailServiceImpl implements ThumbnailService {
    @Autowired
    private DvContentMapper contentMapper;
    @Autowired
    private SysSettingService settingService;
    @Autowired
    private WebSocketServer webSocketServer;


    /**
     * 清理失效缩略图
     * @author jitwxs
     * @since 2018/10/4 2:20
     */
    @Async("taskExecutor")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cleanThumbnailTask(String sessionId) {
        String resThumbnail = settingService.getResThumbnail();

        // 递归查找缩略图根目录下所有jpg文件
        String[] selectSuffix = {"jpg"};
        Collection<File> listFiles = FileUtils.listFiles(new File(resThumbnail), selectSuffix, true);

        int count = 0;
        for (File file : listFiles) {
            String id = FileUtils.getPrefix(file.getName());
            if(contentMapper.selectById(id) == null) {
                FileUtils.deleteQuietly(file);
                count++;
            }
        }

        String message = "清理完成，共清理缩略图：" + count + " 个";
        webSocketServer.sendMessage(message, sessionId);
    }

    @Override
    public String getUrl(String path) {
        // 替换反斜杠
        path = path.replace("\\", "/");

        return settingService.getThumbnailIp() + "/" + path;
    }
}
