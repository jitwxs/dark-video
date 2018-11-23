package jit.wxs.dv.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvContentCommentService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 评论Controller
 * @author jitwxs
 * @since 2018/10/5 23:06
 */
@Api(tags = {"评论管理"})
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private DvContentCommentService contentCommentService;

    @ApiOperation(value= "计算评论数")
    @ApiImplicitParam(paramType = "path", name="contentId", value="内容ID", required = true)
    @GetMapping(value= "/{contentId}/count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO countByContentId(@PathVariable String contentId) {
        return ResultVOUtils.success(contentCommentService.countByContentId(contentId));
    }

    @ApiOperation(value= "发表评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name="contentId", value="内容ID", required = true),
            @ApiImplicitParam(paramType = "form", name="content", value="评论内容", required = true)
    })
    @PostMapping(value= "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO publishComment(String contentId, String content) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return contentCommentService.publishComment(contentId, username, content);
    }

    @ApiOperation(value= "删除评论", notes = "限制管理员和自己可删除")
    @ApiImplicitParam(paramType = "path", name="id", value="评论ID", required = true)
    @DeleteMapping(value= "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO deleteComment(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return contentCommentService.deleteComment(id, username);
    }
}
