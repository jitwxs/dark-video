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
     * 资源根路径
     */
    @Value("${project.setting.key.res-root}")
    private String resRootKey;
    /**
     * 资源服务器IP
     */
    @Value("${project.setting.key.res-ip}")
    private String resIpKey;

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
