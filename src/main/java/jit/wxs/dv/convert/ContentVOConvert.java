package jit.wxs.dv.convert;

import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.domain.vo.ContentVO;
import jit.wxs.dv.service.ThumbnailService;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jitwxs
 * @since 2018/10/5 10:57
 */
@Component
public class ContentVOConvert {

    @Autowired
    private ThumbnailService thumbnailService;

    public ContentVO convert(DvContent content) {
        ContentVO contentVO = new ContentVO();
        BeanUtils.copyProperties(content, contentVO);

        // 去除文件名后缀
        contentVO.setName(StringUtils.getPrefix(contentVO.getName()));

        // 获取缩略图Url
        String thumbnailPath = content.getThumbnail();
        if(StringUtils.isNotBlank(thumbnailPath)) {
            contentVO.setThumbnail(thumbnailService.getUrl(thumbnailPath));
        }

        return contentVO;
    }

    public List<ContentVO> convert(List<DvContent> list) {
        List<ContentVO> contentVOS = new LinkedList<>();

        for(DvContent content : list) {
            contentVOS.add(convert(content));
        }

        return contentVOS;
    }
}
