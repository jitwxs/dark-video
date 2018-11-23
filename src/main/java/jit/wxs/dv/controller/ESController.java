package jit.wxs.dv.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.ESService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * ES Controller
 * @author jitwxs
 * @since 2018/10/9 22:35
 */
@Api(tags = {"索引管理"})
@RestController
@RequestMapping("/es")
public class ESController {
    @Autowired
    private ESService esService;

    @ApiOperation(value= "内容索引是否存在")
    @GetMapping(value = "/content/exist", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO hasContentExist() {
        boolean hasExist = esService.hasContentIndexExist();
        return hasExist ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.CONTENT_INDEX_NOT_EXIST);
    }

    @ApiOperation(value= "创建内容索引")
    @PostMapping(value = "/content/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO createContentIndex() {
        return esService.createContentIndex();
    }

    @ApiOperation(value= "生成内容索引", notes = "基于WebSocket")
    @PostMapping(value = "/content/build", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO buildContentIndex(@ApiIgnore HttpSession session) {
        esService.buildContentIndex(session.getId());

        return ResultVOUtils.successWithMsg("开始生成内容索引");
    }
}

