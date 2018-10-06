package jit.wxs.dv.service;

import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.DvContentComment;
import jit.wxs.dv.domain.vo.ResultVO;

import java.util.List;

/**
 * <p>
 * 内容评论表 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-05
 */
public interface DvContentCommentService extends IService<DvContentComment> {
    /**
     * 获取Content评论数
     * @author jitwxs
     * @since 2018/10/5 23:07
     */
    int countByContentId(String contentId);

    /**
     * 读取Content所有评论
     * @author jitwxs
     * @since 2018/10/5 23:34
     */
    List<DvContentComment> listByContentId(String contentId);

    /**
     * 删除评论
     * @author jitwxs
     * @since 2018/10/5 23:08
     */
    ResultVO deleteComment(String id, String username);
    
    /**
     * 发表评论
     * @author jitwxs
     * @since 2018/10/5 23:09
     */
    ResultVO publishComment(String contentId, String username, String content);
}
