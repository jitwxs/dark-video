package jit.wxs.dv.domain.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 内容表
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
@Data
public class DvContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 所属分类
     */
    private String category;
    /**
     * 父路径
     */
    private String parentPath;
    /**
     * 类型
     */
    private String type;
    /**
     * 大小
     */
    private String size;
    /**
     * 缩略图路径
     */
    private String thumbnail;
    /**
     * 时长
     */
    private String duration;
    /**
     * 分辨率
     */
    private String resolution;
    /**
     * 帧率
     */
    private String frameRate;
    /**
     * 上传用户
     */
    private String author;
    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
}
