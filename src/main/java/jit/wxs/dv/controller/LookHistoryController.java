package jit.wxs.dv.controller;

import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.DvContentLookHistoryService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 观看历史
 * @author jitwxs
 * @since 2018/10/14 18:16
 */
@RestController
@RequestMapping("/content/history")
public class LookHistoryController {
    @Autowired
    private DvContentLookHistoryService lookHistoryService;

    @DeleteMapping("/{id}")
    public ResultVO deleteHistory(@PathVariable String id) {
        boolean b = lookHistoryService.deleteById(id);

        return b ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.DELETE_RECORD_ERROR);
    }
}
