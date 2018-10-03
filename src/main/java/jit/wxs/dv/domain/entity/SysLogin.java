package jit.wxs.dv.domain.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
@Data
public class SysLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableId
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 角色ID
     */
    private Integer roleId;
}
