package jit.wxs.dv.domain.entity;

import lombok.Data;

/**
 * @author jitwxs
 * @since 2018/10/9 23:57
 */
@Data
public class ESContent {
    private String id;

    private String name;

    private String thumbnail;

    private String createDate;

    private Integer type;

    private ESContent parent;
}
