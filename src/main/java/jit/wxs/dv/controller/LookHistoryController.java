package jit.wxs.dv.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvContentLookHistoryService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 观看历史
 * @author jitwxs
 * @since 2018/10/14 18:16
 */
@Api(tags = {"观看历史管理"})
@RestController
@RequestMapping("/content/history")
public class LookHistoryController {
    @Autowired
    private DvContentLookHistoryService lookHistoryService;

    @ApiOperation(value= "删除历史")
    @ApiImplicitParam(paramType = "path", name="id", value="历史纪录ID", required = true)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO deleteHistory(@PathVariable String id) {
        boolean b = lookHistoryService.deleteById(id);

        return b ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.DELETE_RECORD_ERROR);
    }
}
