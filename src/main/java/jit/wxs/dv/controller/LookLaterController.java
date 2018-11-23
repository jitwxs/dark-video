package jit.wxs.dv.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvContentLookLaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * 稍后再看Controller
 * @author jitwxs
 * @since 2018/10/7 22:27
 */
@Api(tags = {"稍后再看管理"})
@Validated
@RestController
@RequestMapping("/content/later")
public class LookLaterController {
    @Autowired
    private DvContentLookLaterService lookLaterService;

    @ApiOperation(value= "添加到稍后再看")
    @ApiImplicitParam(paramType = "form", name="contentId", value="内容ID", required = true)
    @PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO addLookLater(@NotBlank(message = "内容不能为空") String contentId) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        return lookLaterService.addLookLater(contentId, username);
    }

    @ApiOperation(value= "删除稍后再看记录")
    @ApiImplicitParam(paramType = "path", name="id", value="稍后再看记录ID", required = true)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO deleteLookLater(@PathVariable String id) {
        return lookLaterService.deleteLookLater(id);
    }
}
