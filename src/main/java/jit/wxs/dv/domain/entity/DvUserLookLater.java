package jit.wxs.dv.domain.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import jit.wxs.dv.domain.vo.ContentVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 稍后再看表
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-07
 */
@Data
public class DvUserLookLater implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字符串ID
     */
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

    private Date createDate;

    @TableField(exist = false)
    private ContentVO contentVO;

    public DvUserLookLater(String username, String contentId) {
        this.username = username;
        this.contentId = contentId;
        this.createDate = new Date();
    }
}
