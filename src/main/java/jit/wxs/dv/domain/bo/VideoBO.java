package jit.wxs.dv.domain.bo;

import lombok.Data;

/**
 * 视频内容前台展示
 * @author jitwxs
 * @since 2018/10/5 17:21
 */
@Data
public class VideoBO extends ContentBO {
    private String size;

    private String thumbnail;

    private String duration;

    private String resolution;

    private String frameRate;
}
