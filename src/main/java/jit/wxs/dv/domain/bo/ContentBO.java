package jit.wxs.dv.domain.bo;

import lombok.Data;

import java.util.Date;

/**
 * 通用的内容前台展示
 * @author jitwxs
 * @since 2018/10/5 17:19
 */
@Data
public class ContentBO {
    private String id;

    private String name;

    private String type;

    private String author;

    private Date createDate;

    private String suffix;

    private String url;
}