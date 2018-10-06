package jit.wxs.dv.domain.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 内容评论表
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-05
 */
@Data
public class DvContentComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字符串ID
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 所属内容
     */
    private String contentId;
    /**
     * 评论内容（255内）
     */
    private String content;
    /**
     * 发表用户
     */
    private String username;
    /**
     * 发表时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    public DvContentComment(String contentId, String content, String username) {
        this.contentId = contentId;
        this.content = content;
        this.username = username;
        this.createDate = new Date();
    }
}
