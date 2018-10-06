package jit.wxs.dv.controller;

import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvContentCommentService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 评论Controller
 * @author jitwxs
 * @since 2018/10/5 23:06
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private DvContentCommentService contentCommentService;

    /**
     * 计算评论数
     * @author jitwxs
     * @since 2018/10/5 23:19
     */
    @GetMapping("/{contentId}/count")
    public ResultVO countByContentId(@PathVariable String contentId) {
        if(StringUtils.isBlank(contentId)) {
            return ResultVOUtils.error(ResultEnum.PARAM_ERROR);
        }

        int i = contentCommentService.countByContentId(contentId);

        return ResultVOUtils.success(i);
    }

    /**
     * 发表评论
     * @author jitwxs
     * @since 2018/10/5 23:20
     */
    @PostMapping("")
    public ResultVO publishComment(String contentId, String content) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return contentCommentService.publishComment(contentId, username, content);
    }

    /**
     * 删除评论
     * 限制管理员和自己可删除
     * @author jitwxs
     * @since 2018/10/5 23:25
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteComment(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return contentCommentService.deleteComment(id, username);
    }
}
