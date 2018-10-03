package jit.wxs.dv.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.config.SettingConfig;
import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.mapper.DvContentMapper;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 内容表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
@Slf4j
@Service
public class DvContentServiceImpl extends ServiceImpl<DvContentMapper, DvContent> implements DvContentService {
    @Autowired
    private DvContentMapper contentMapper;

    @Autowired
    private SysSettingService settingService;

    @Autowired
    private SettingConfig settingConfig;

    @Override
    public void genContent(String path, String url, String parentId) {
        File pathFile = new File(path);
        Collection<File> listFiles = FileUtils.listFiles(pathFile, null, false);

        // 获取数据库中原始内容，用于删除失效内容
        List<String> dbIds = contentMapper.listIdsByCategory(parentId);

        for(File file : listFiles) {
            // 设置ID为该文件Url的MD5值
            String id = DigestUtils.md5Hex(url + File.separator + file.getName());

            // 判断ID是否存在，如果存在，跳过循环
            if(contentMapper.selectById(id) != null) {
                // 从集合中移除
                dbIds.remove(id);
                continue;
            }

            DvContent content = parserContent(id, parentId, url, file);
            contentMapper.insert(content);
        }

        // 删除失效的内容数据
        for(String id : dbIds) {
            contentMapper.deleteById(id);
        }
    }

    /**
     * 解析并生成Content对象
     * @param file 文件对象
     * @param id contentId
     * @param parentId 所属目录Id
     * @param path 所属路径
     * @author jitwxs
     * @since 2018/8/25 3:42
     */
    private DvContent parserContent(String id, String parentId, String path, File file) {
        DvContent content = new DvContent();
        content.setId(id);
        content.setName(file.getName());
        content.setCategory(parentId);
        content.setParentPath(path);

        String suffix = FileUtils.getSuffix(file.getName());
        Map<String, Object> videoInfo = null;
        // 设置类别
        if(ArrayUtils.contains(settingConfig.getVideo(), suffix)) {
            videoInfo = FileUtils.getVideoInfo(file.getPath());
            content.setType("video");
            // 大小
            content.setSize(FileUtils.formatSize(FileUtils.sizeOf(file)));
            // 视频时长
            content.setDuration((String)videoInfo.get("durationStr"));
            // 帧率
            content.setFrameRate((String)videoInfo.get("frameRate"));
            // 分辨率
            content.setResolution((String)videoInfo.get("resolution"));
            // 创建时间
            content.setCreateDate((Date)videoInfo.get("createTime"));
        } else if(ArrayUtils.contains(settingConfig.getPicture(), suffix)) {
            content.setType("picture");
        } else if(ArrayUtils.contains(settingConfig.getMusic(), suffix)) {
            content.setType("music");
            // 设置大小
            content.setSize(FileUtils.formatSize(FileUtils.sizeOf(file)));
        } else if(ArrayUtils.contains(settingConfig.getPage(), suffix)) {
            content.setType("page");
        }  else if(ArrayUtils.contains(settingConfig.getText(), suffix)) {
            content.setType("text");
        } else {
            content.setType("other");
        }

        // 设置视频和图片缩略图
        if ("video".equals(content.getType()) || "picture".equals(content.getType())) {
            // 缩略图目标文件夹 /Thumbnail/id[:2]
            String relativePath = File.separator + "Thumbnail" + File.separator + content.getId().substring(0,2);
            // 缩略图完整文件夹完整路径 resRoot/Thumbnail/id[:2]
            File thumbnailDir = new File(settingService.getResRoot() + relativePath);

            // 当前文件缩略图路径 resRoot/Thumbnail/id[:2]/id,jpg
            String thumbnailPath = thumbnailDir + File.separator + content.getId() + ".jpg";

            try {
                // 如果缩略图不存在，生成缩略图
                if (!FileUtils.directoryContains(thumbnailDir, new File(thumbnailPath))) {
                    switch (content.getType()) {
                        case "video":
                            FileUtils.fetchFrame(file.getPath(),thumbnailPath, (int)videoInfo.get("duration"), (String)videoInfo.get("resolution"));
                            log.info("新增视频缩略图：{}", thumbnailPath);
                            break;
                        case "picture":
                            // （256 * n） 16:9
                            FileUtils.resizeImage(file.getPath(), thumbnailPath, 256);
                            log.info("新增图片缩略图：{}", thumbnailPath);
                            break;
                        default:
                            break;
                    }
                }

                String dbThumbnail = relativePath + File.separator + content.getId() + ".jpg";
                content.setThumbnail(dbThumbnail);
            } catch (Exception e) {
                log.error("生成缩略图出现异常，异常文件：{}，期望保存文件：{}", file.getPath(), thumbnailPath);
            }
        }

        return content;
    }
}
