package jit.wxs.dv.domain.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jit.wxs.dv.domain.vo.ContentVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 观看历史表
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-14
 */
@Data
public class DvContentLookHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 内容ID
     */
    private String contentId;
    /**
     * 观看时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lookDate;

    @TableField(exist = false)
    private ContentVO contentVO;

    public DvContentLookHistory(String username, String contentId) {
        this.username = username;
        this.contentId = contentId;
        this.lookDate = new Date();
    }
}
