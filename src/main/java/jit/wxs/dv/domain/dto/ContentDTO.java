package jit.wxs.dv.domain.dto;

import jit.wxs.dv.domain.entity.DvContent;
import lombok.Data;

/**
 * @author jitwxs
 * @since 2018/10/5 11:16
 */
@Data
public class ContentDTO extends DvContent {
    /**
     * 后缀
     * @author jitwxs
     * @since 2018/10/5 11:16
     */
    private String suffix;
    /**
     * Url
     * @author jitwxs
     * @since 2018/10/5 11:16
     */
    private String url;
}
