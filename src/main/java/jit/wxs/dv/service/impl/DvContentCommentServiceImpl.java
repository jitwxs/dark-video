package jit.wxs.dv.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.domain.entity.DvContentComment;
import jit.wxs.dv.domain.entity.SysLogin;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.enums.RoleEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.mapper.DvContentCommentMapper;
import jit.wxs.dv.mapper.DvContentMapper;
import jit.wxs.dv.mapper.SysLoginMapper;
import jit.wxs.dv.service.DvContentCommentService;
import jit.wxs.dv.service.SysLoginService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 内容评论表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-05
 */
@Service
public class DvContentCommentServiceImpl extends ServiceImpl<DvContentCommentMapper, DvContentComment> implements DvContentCommentService {
    @Autowired
    private DvContentCommentMapper contentCommentMapper;
    @Autowired
    private DvContentMapper contentMapper;
    @Autowired
    private SysLoginMapper loginMapper;

    @Override
    public int countByContentId(String contentId) {
        return contentCommentMapper.countByContentId(contentId);
    }

    @Override
    public List<DvContentComment> listByContentId(String contentId) {
        return contentCommentMapper.selectList(new EntityWrapper<DvContentComment>()
                .eq("content_id", contentId)
                .orderBy("create_date", true));
    }

    @Override
    public ResultVO deleteComment(String id, String username) {
        // 判断权限
        if(!hasPermission(id, username)) {
            return ResultVOUtils.error(ResultEnum.PERMISSION_ERROR);
        }

        Integer integer = contentCommentMapper.deleteById(id);

        return integer == 1 ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.COMMENT_DEL_ERROR);
    }

    @Override
    public ResultVO publishComment(String contentId, String username, String content) {
        if(StringUtils.isBlank(contentId, username, content)) {
            return ResultVOUtils.error(ResultEnum.PARAM_ERROR);
        }
        //内容不得大于255
        if(content.length() > 255) {
            return ResultVOUtils.error(ResultEnum.COMMENT_TOO_LONG);
        }

        // 判断内容是否存在
        if(!contentMapper.hasExist(contentId)) {
            return ResultVOUtils.error(ResultEnum.CONTENT_NOT_EXIST);
        }

        DvContentComment contentComment = new DvContentComment(contentId, content, username);
        Integer insert = contentCommentMapper.insert(contentComment);

        return insert == 1 ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.COMMENT_PUBLISH_ERROR);
    }

    public boolean hasPermission(String id, String username) {
        SysLogin sysLogin = loginMapper.selectById(username);

        RoleEnum roleEnum = RoleEnum.getEnum(sysLogin.getRoleId());
        if(roleEnum == null) {
            return false;
        }

        // 管理员拥有权限
        if(roleEnum == RoleEnum.ROLE_ADMIN) {
            return true;
        }

        // 本人拥有权限
        return contentCommentMapper.hasBelong(id, username);
    }
}
