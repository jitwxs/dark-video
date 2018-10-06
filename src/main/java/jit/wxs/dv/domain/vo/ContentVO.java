package jit.wxs.dv.domain.vo;

import lombok.Data;

/**
 * @author jitwxs
 * @since 2018/10/5 10:55
 */
@Data
public class ContentVO {
    private String id;

    private String name;

    private String thumbnail;

    private String type;

    private String duration;

    private Integer commentCount;
}
