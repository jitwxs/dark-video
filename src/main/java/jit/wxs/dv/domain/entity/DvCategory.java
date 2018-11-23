package jit.wxs.dv.domain.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 类别表
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
@ApiModel("分类实体")
@Data
public class DvCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 类别ID
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty("类别ID")
    private String id;
    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;
    /**
     * 父分类名称
     */
    @ApiModelProperty("父分类名称")
    private String parentId;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
}
