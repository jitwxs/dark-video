package jit.wxs.dv.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author jitwxs
 * @since 2018/6/17 12:35
 */
@Configuration
@Getter
@Setter
public class SettingConfig {
    /**
     * 内容资源目录
     */
    @Value("${project.setting.key.res-content}")
    private String resContent;
    /**
     * 缩略图资源目录
     */
    @Value("${project.setting.key.res-thumbnail}")
    private String resThumbnail;
    /**
     * 内容资源IP
     */
    @Value("${project.setting.key.res-content-ip}")
    private String contentIp;
    /**
     * 缩略图资源IP
     */
    @Value("${project.setting.key.res-thumbnail-ip}")
    private String thumbnailIp;
    @Value("${project.setting.key.video-num}")
    private String videoNum;

    @Value("${project.setting.key.picture-num}")
    private String pictureNum;

    @Value("${server.url}")
    private String serverUrl;

    public final String[] video = {
      "mp4","wmv","mkv","mpg","avi","flv","rm","rmvb","mpg","mov","m4v","3gp"

    };
    public final String[] picture = {
            "jpg","png","gif","jpeg","bmp"
    };
    public final String[] music = {
            "mp3","m4a"
    };
    public final String[] page = {
            "htm","html"
    };
    public final String[] text = {
            "txt","js", "css"
    };
}
