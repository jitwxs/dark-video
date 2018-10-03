package jit.wxs.dv.domain.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  系统设置键值对
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
@Data
@AllArgsConstructor
public class SysSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String k;
    private String v;
}
