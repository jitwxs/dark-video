package jit.wxs.dv.service;

import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.SysUser;
import jit.wxs.dv.domain.vo.ResultVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 重置密码
     * @param username 用户名
     * @param rawPassword 原始密码
     * @param newPassword 新密码
     * @author jitwxs
     * @since 2018/10/14 18:44
     */
    ResultVO resetPassword(String username, String rawPassword, String newPassword);
}
