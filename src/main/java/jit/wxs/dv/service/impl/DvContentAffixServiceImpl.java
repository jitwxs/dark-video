package jit.wxs.dv.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.config.SettingConfig;
import jit.wxs.dv.domain.entity.DvContentAffix;
import jit.wxs.dv.mapper.DvContentAffixMapper;
import jit.wxs.dv.service.DvContentAffixService;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 内容附件【三级目录内所有内容】表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
@Slf4j
@Service
public class DvContentAffixServiceImpl extends ServiceImpl<DvContentAffixMapper, DvContentAffix> implements DvContentAffixService {
    @Autowired
    private DvContentAffixMapper contentAffixMapper;
    @Autowired
    private SysSettingService sysSettingService;

    @Autowired
    private SettingConfig settingConfig;

    @Override
    public void genContentAffix(File rootFile, String contentId) {
        String resContent = sysSettingService.getResContent();

        // 获取数据库中原始内容，用于删除失效内容
        List<String> dbIds = contentAffixMapper.listIdsByContentId(contentId);

        // 递归获取所有文件
        for(File file : FileUtils.listFiles(rootFile, null, true)) {
            String url = file.getPath().replace(resContent, "");
            // 设置ID为该文件Url的MD5值
            String id = DigestUtils.md5Hex( url+ File.separator + file.getName());

            // 判断ID是否存在，如果存在，跳过循环
            if(contentAffixMapper.selectById(id) != null) {
                // 从集合中移除
                dbIds.remove(id);
                continue;
            }

            DvContentAffix contentAffix = parserContentAffix(id,contentId, url,file);
            contentAffixMapper.insert(contentAffix);
        }

        // 删除失效的内容数据
        for(String id : dbIds) {
            contentAffixMapper.deleteById(id);
        }
    }

    @Override
    public List<DvContentAffix> listByContentId(String contentId) {
        return contentAffixMapper.selectList(new EntityWrapper<DvContentAffix>().eq("content_id", contentId));
    }

    private DvContentAffix parserContentAffix(String id, String contentId, String path, File file) {
        DvContentAffix contentAffix = new DvContentAffix();
        contentAffix.setId(id);
        contentAffix.setName(file.getName());
        contentAffix.setContentId(contentId);
        contentAffix.setPath(path);

        // 设置类别
        String suffix = FileUtils.getSuffix(file.getName());
        Map<String, Object> videoInfo = null;
        if(ArrayUtils.contains(settingConfig.getVideo(), suffix)) {
            videoInfo = FileUtils.getVideoInfo(file.getPath());
            contentAffix.setType("video");
            // 大小
            contentAffix.setSize(FileUtils.formatSize(FileUtils.sizeOf(file)));
            // 视频时长
            contentAffix.setDuration((String)videoInfo.get("durationStr"));
            // 帧率
            contentAffix.setFrameRate((String)videoInfo.get("frameRate"));
            // 分辨率
            contentAffix.setResolution((String)videoInfo.get("resolution"));
        } else if(ArrayUtils.contains(settingConfig.getPicture(), suffix)) {
            contentAffix.setType("picture");
        } else if(ArrayUtils.contains(settingConfig.getMusic(), suffix)) {
            contentAffix.setType("music");
            // 设置大小
            contentAffix.setSize(FileUtils.formatSize(FileUtils.sizeOf(file)));
        } else if(ArrayUtils.contains(settingConfig.getPage(), suffix)) {
            contentAffix.setType("page");
        }  else if(ArrayUtils.contains(settingConfig.getText(), suffix)) {
            contentAffix.setType("text");
        } else {
            contentAffix.setType("other");
        }

        // 设置视频缩略图
        if ("video".equals(contentAffix.getType())) {
            // 缩略图目标文件夹 /id[:2]
            String relativePath = File.separator + contentAffix.getId().substring(0,2);
            // 缩略图完整文件夹完整路径 resRoot/Thumbnail/id[:2]
            File thumbnailDir = new File(sysSettingService.getResThumbnail() + relativePath);

            // 当前文件缩略图路径 resThumbnail/id[:2]/id.jpg
            String thumbnailPath = thumbnailDir + File.separator + contentAffix.getId() + ".jpg";

            try {
                // 如果缩略图不存在，生成缩略图
                if (!FileUtils.directoryContains(thumbnailDir, new File(thumbnailPath))) {
                    switch (contentAffix.getType()) {
                        case "video":
                            FileUtils.fetchFrame(file.getPath(),thumbnailPath, (int)videoInfo.get("duration"), "200x112");
                            log.info("新增视频缩略图：{}", thumbnailPath);
                            break;
                        default:
                            break;
                    }
                }

                String dbThumbnail = relativePath + File.separator + contentAffix.getId() + ".jpg";
                contentAffix.setThumbnail(dbThumbnail);
            } catch (Exception e) {
                log.error("生成缩略图出现异常，异常文件：{}，期望保存文件：{}", file.getPath(), thumbnailPath);
            }
        }

        return contentAffix;
    }
}
