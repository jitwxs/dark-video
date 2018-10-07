package jit.wxs.dv.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import jit.wxs.dv.domain.entity.SysLogin;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
public interface SysLoginMapper extends BaseMapper<SysLogin> {
    /**
     * 统计用户数
     * @author jitwxs
     * @since 2018/10/7 17:20
     */
    int countUser();
}
