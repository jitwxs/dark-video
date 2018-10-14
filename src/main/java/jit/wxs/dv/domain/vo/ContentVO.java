package jit.wxs.dv.domain.vo;

import lombok.Data;

import java.util.Date;

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

    /**
     * 评论数
     */
    private Integer commentCount;

    private Integer click;

    private Date createDate;

    private String desc;

    /**
     * 上传者
     */
    private String author;

    /**
     * 是否是稍后再看
     */
    private Boolean hasLookAfter;
    /**
     * 一级分类
     */
    private String firstCategoryName;
    /**
     * 二级分类
     */
    private String secondCategoryName;
}
