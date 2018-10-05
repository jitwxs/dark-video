package jit.wxs.dv.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.config.SettingConfig;
import jit.wxs.dv.convert.ContentVOConvert;
import jit.wxs.dv.domain.dto.ContentDTO;
import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.domain.enums.CategoryLevelEnum;
import jit.wxs.dv.domain.vo.ContentVO;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.mapper.DvContentMapper;
import jit.wxs.dv.service.DvContentAffixService;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.service.ThumbnailService;
import jit.wxs.dv.util.FileUtils;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.File;
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
    private DvContentAffixService contentAffixService;
    @Autowired
    private SysSettingService settingService;
    @Autowired
    private ThumbnailService thumbnailService;
    @Autowired
    private SettingConfig settingConfig;
    @Autowired
    private ContentVOConvert contentVOConvert;

    @Override
    public void genContent(String path, String url, String firstCategory, String secondCategory, boolean hasSecondCategory) {
        // 获取所有文件
        for(File file : FileUtils.listFiles(new File(path), null, false)) {
            // 设置ID为该文件Url的MD5值
            String id = DigestUtils.md5Hex(url + File.separator + file.getName());

            if(contentMapper.selectById(id) == null) {
                DvContent content = parserContent(id, firstCategory, secondCategory, url, file);
                contentMapper.insert(content);
            }
        }

        // 如果是二级目录下，还要获取所有子文件夹，作为内容，相应子文件夹的所有内容作为附件存入dv_content_affix表
        if(hasSecondCategory) {
            for(File file : FileUtils.listDir(path)) {
                // 设置ID为该文件Url的MD5值
                String id = DigestUtils.md5Hex(url + File.separator + file.getName());

                if(contentMapper.selectById(id) == null) {
                    DvContent content = parserContent(id, firstCategory, secondCategory, url, file);
                    contentMapper.insert(content);
                }

                // 插入附件信息
                contentAffixService.genContentAffix(file, id);
            }
        }
    }

    @Override
    public ResultVO listLatestContent(CategoryLevelEnum levelEnum, String category, int count) {
        List<DvContent> selectList = contentMapper.selectList(new EntityWrapper<DvContent>()
                .eq(levelEnum.getMessage(), category)
                .orderBy("create_date",false)
                .last("LIMIT " + count));

        return ResultVOUtils.success(contentVOConvert.convert(selectList));
    }

    @Override
    public ResultVO listHotContent(CategoryLevelEnum levelEnum, String category, int count) {
        // TODO hot
        List<DvContent> selectList = contentMapper.selectList(new EntityWrapper<DvContent>()
                .eq(levelEnum.getMessage(), category)
//                .orderBy("create_date",false)
                .last("LIMIT " + count));

        return ResultVOUtils.success(contentVOConvert.convert(selectList));
    }

    @Override
    public Page<ContentVO> pageByCategory(CategoryLevelEnum levelEnum, String category, Page<DvContent> page) {
        List<DvContent> records = contentMapper.pageByCategory(levelEnum.getMessage(), category, page);

        Page<ContentVO> page1 = new Page<>();
        BeanUtils.copyProperties(page, page1);
        page1.setRecords(contentVOConvert.convert(records));

        return page1;
    }

    @Override
    public ContentDTO getContentDTO(String contentId) {
        DvContent content = contentMapper.selectById(contentId);
        ContentDTO contentDTO = new ContentDTO();
        BeanUtils.copyProperties(content, contentDTO);

        // 设置Url
        if("video".equals(contentDTO.getType())) {
            contentDTO.setUrl(settingService.getContentIp() + "/" + contentDTO.getPath());
        }

        // 获取缩略图Url
        String thumbnailPath = contentDTO.getThumbnail();
        if(StringUtils.isNotBlank(thumbnailPath)) {
            contentDTO.setThumbnail(thumbnailService.getUrl(thumbnailPath));
        }

        // 设置后缀
        contentDTO.setSuffix(StringUtils.getSuffix(contentDTO.getName()));
        // 设置去除后缀后的文件名
        contentDTO.setName(StringUtils.getPrefix(contentDTO.getName()));

        return contentDTO;
    }

    /**
     * 解析并生成Content对象
     * @param file 文件对象
     * @param id contentId
     * @param firstCategory 所属一级目录
     * @param secondCategory 所属二级目录
     * @param path 所属路径
     * @author jitwxs
     * @since 2018/8/25 3:42
     */
    private DvContent parserContent(String id, String firstCategory, String secondCategory, String path, File file) {
        DvContent content = new DvContent();
        // 设置基础信息
        content.setId(id);
        content.setName(file.getName());
        content.setFirstCategory(firstCategory);
        content.setSecondCategory(secondCategory);
        content.setPath(path + File.separator + file.getName());
        String author = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        content.setAuthor(author);
        content.setCreateDate(new Date());

        // 设置类别
        if(file.isDirectory()) {
            content.setType("dir");
            return content;
        }

        String suffix = FileUtils.getSuffix(file.getName());
        Map<String, Object> videoInfo = null;
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

        // 设置视频缩略图
        if ("video".equals(content.getType())) {
            // 缩略图目标文件夹 /id[:2]
            String relativePath = File.separator + content.getId().substring(0,2);
            // 缩略图完整文件夹完整路径 resRoot/Thumbnail/id[:2]
            File thumbnailDir = new File(settingService.getResThumbnail() + relativePath);

            // 当前文件缩略图路径 resThumbnail/id[:2]/id.jpg
            String thumbnailPath = thumbnailDir + File.separator + content.getId() + ".jpg";

            try {
                // 判断文件夹是否存在
                if(!thumbnailDir.exists()) {
                    FileUtils.forceMkdir(thumbnailDir);
                }

                // 如果缩略图不存在，生成缩略图
                if (!FileUtils.directoryContains(thumbnailDir, new File(thumbnailPath))) {
                    switch (content.getType()) {
                        case "video":
                            FileUtils.fetchFrame(file.getPath(),thumbnailPath, (int)videoInfo.get("duration"), "200x112");
                            log.info("新增视频缩略图：{}", thumbnailPath);
                            break;
                        default:
                            break;
                    }
                }

                String dbThumbnail = relativePath + File.separator + content.getId() + ".jpg";
                content.setThumbnail(dbThumbnail);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("生成缩略图出现异常，异常文件：{}，期望保存文件：{}", file.getPath(), thumbnailPath);
            }
        }

        return content;
    }
}
