package jit.wxs.dv.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.domain.entity.SysUser;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.mapper.SysUserMapper;
import jit.wxs.dv.service.SysUserService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public ResultVO resetPassword(String username, String rawPassword, String newPassword) {
        SysUser user = userMapper.selectById(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(user == null) {
            return ResultVOUtils.error(ResultEnum.AUTHORITY_ERROR);
        }

        if(!encoder.matches(rawPassword, user.getPassword())) {
            return ResultVOUtils.error(ResultEnum.RAW_PASSWORD_ERROR);
        }

        user.setPassword(encoder.encode(newPassword));
        userMapper.updateById(user);

        return ResultVOUtils.success();
    }
}
