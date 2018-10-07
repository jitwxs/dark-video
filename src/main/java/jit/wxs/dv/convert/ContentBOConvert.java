package jit.wxs.dv.convert;

import jit.wxs.dv.domain.bo.ContentBO;
import jit.wxs.dv.domain.bo.PictureBO;
import jit.wxs.dv.domain.bo.VideoBO;
import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.domain.entity.DvContentAffix;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.service.ThumbnailService;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jitwxs
 * @since 2018/10/5 17:25
 */
@Component
public class ContentBOConvert {
    @Autowired
    private SysSettingService settingService;
    @Autowired
    private ThumbnailService thumbnailService;

    public ContentBO convert2BO(DvContent content) {
        ContentBO contentBO = new ContentBO();
        BeanUtils.copyProperties(content, contentBO);

        // 设置Url
        contentBO.setUrl(settingService.getContentIp() + "/" + content.getPath().replace("\\","/"));
        // 设置后缀
        contentBO.setSuffix(StringUtils.getSuffix(content.getName()));
        // 设置去除后缀后的文件名
        contentBO.setName(StringUtils.getPrefix(content.getName()));

        return contentBO;
    }

    public List<ContentBO> convert2BO(List<DvContent> contents) {
        List<ContentBO> list = new LinkedList<>();
        for(DvContent content : contents) {
            list.add(convert2BO(content));
        }
        return list;
    }

    public ContentBO convertAffix2BO(DvContentAffix contentAffix, Date createDate, String author) {
        DvContent content = new DvContent();
        BeanUtils.copyProperties(contentAffix, content);
        content.setCreateDate(createDate);
        content.setAuthor(author);

        return convert2BO(content);
    }

    public List<ContentBO> convertAffix2BO(List<DvContentAffix> contentAffixes, Date createDate, String author) {
        List<ContentBO> list = new LinkedList<>();
        for(DvContentAffix affix : contentAffixes) {
            list.add(convertAffix2BO(affix, createDate, author));
        }
        return list;
    }

    public VideoBO convert2Video(DvContent content) {
        VideoBO videoBO = new VideoBO();
        BeanUtils.copyProperties(content, videoBO);

        // 设置Url
        videoBO.setUrl(settingService.getContentIp() + "/" + content.getPath().replace("\\","/"));
        // 设置后缀
        videoBO.setSuffix(StringUtils.getSuffix(content.getName()));
        // 设置去除后缀后的文件名
        videoBO.setName(StringUtils.getPrefix(content.getName()));
        // 设置缩略图Url
        String thumbnailPath = content.getThumbnail();
        if(StringUtils.isNotBlank(thumbnailPath)) {
            videoBO.setThumbnail(thumbnailService.getUrl(thumbnailPath));
        }

        return videoBO;
    }

    public List<VideoBO> convert2Video(List<DvContent> contents) {
        List<VideoBO> list = new LinkedList<>();
        for(DvContent content : contents) {
            list.add(convert2Video(content));
        }
        return list;
    }

    public VideoBO convertAffix2Video(DvContentAffix contentAffix, Date createDate, String author) {
        DvContent content = new DvContent();
        BeanUtils.copyProperties(contentAffix, content);
        content.setCreateDate(createDate);
        content.setAuthor(author);

        return convert2Video(content);
    }

    public List<VideoBO> convertAffix2Video(List<DvContentAffix> contentAffixes, Date createDate, String author) {
        List<VideoBO> list = new LinkedList<>();
        for(DvContentAffix affix : contentAffixes) {
            list.add(convertAffix2Video(affix, createDate, author));
        }
        return list;
    }

    public PictureBO convert2Picture(DvContent content) {
        PictureBO pictureBO = new PictureBO();
        BeanUtils.copyProperties(content, pictureBO);

        // 设置Url
        pictureBO.setUrl(settingService.getContentIp() + "/" + content.getPath().replace("\\","/"));
        // 设置后缀
        pictureBO.setSuffix(StringUtils.getSuffix(content.getName()));
        // 设置去除后缀后的文件名
        pictureBO.setName(StringUtils.getPrefix(content.getName()));
        // 设置缩略图Url
        String thumbnailPath = content.getThumbnail();
        if(StringUtils.isNotBlank(thumbnailPath)) {
            pictureBO.setThumbnail(thumbnailService.getUrl(thumbnailPath));
        }

        return pictureBO;
    }

    public List<PictureBO> convert2Picture(List<DvContent> contents) {
        List<PictureBO> list = new LinkedList<>();
        for(DvContent content : contents) {
            list.add(convert2Picture(content));
        }
        return list;
    }

    public PictureBO convertAffix2Picture(DvContentAffix pictureAffixes) {
        DvContent content = new DvContent();
        BeanUtils.copyProperties(pictureAffixes, content);

        return convert2Picture(content);
    }

    public List<PictureBO> convertAffix2Picture(List<DvContentAffix> pictureAffixes) {
        List<PictureBO> list = new LinkedList<>();
        for(DvContentAffix affix : pictureAffixes) {
            list.add(convertAffix2Picture(affix));
        }
        return list;
    }
}
