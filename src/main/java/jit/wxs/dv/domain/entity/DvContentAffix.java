package jit.wxs.dv.domain.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 内容附件【三级目录内所有内容】表
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
@Data
public class DvContentAffix implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 内容ID
     */
    private String contentId;
    /**
     * 路径
     */
    private String path;
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
}
