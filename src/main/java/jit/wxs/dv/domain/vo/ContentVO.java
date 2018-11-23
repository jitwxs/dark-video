package jit.wxs.dv.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jitwxs
 * @since 2018/10/5 10:55
 */
@ApiModel("内容VO")
@Data
public class ContentVO {
    @ApiModelProperty("内容ID")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("缩略图")
    private String thumbnail;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("时长【video内容】")
    private String duration;
    @ApiModelProperty("评论数")
    private Integer commentCount;
    @ApiModelProperty("点击数")
    private Integer click;
    @ApiModelProperty("发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    @ApiModelProperty("描述")
    private String desc;
    @ApiModelProperty("发布者")
    private String author;
    @ApiModelProperty("是否是稍后再看")
    private Boolean hasLookAfter;
    @ApiModelProperty("一级分类名")
    private String firstCategoryName;
    @ApiModelProperty("二级分类名")
    private String secondCategoryName;
}
