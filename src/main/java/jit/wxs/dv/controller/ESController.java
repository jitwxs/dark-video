package jit.wxs.dv.controller;

import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.service.ESService;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jitwxs
 * @since 2018/10/9 22:35
 */
@RestController
@RequestMapping("/es")
public class ESController {
    @Autowired
    private ESService esService;

    /**
     * 内容索引是否存在
     * @author jitwxs
     * @since 2018/10/9 22:36
     */
    @GetMapping("/content/exist")
    public ResultVO hasContentExist() {
        boolean hasExist = esService.hasContentIndexExist();

        return hasExist ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.CONTENT_INDEX_NOT_EXIST);
    }

    /**
     * 创建内容索引
     * @author jitwxs
     * @since 2018/10/9 22:36
     */
    @PostMapping("/content/create")
    public ResultVO createContentIndex() {
        return esService.createContentIndex();
    }

    /**
     * 生成内容索引
     * @author jitwxs
     * @since 2018/10/9 23:44
     */
    @PostMapping("/content/build")
    public ResultVO buildContentIndex() {
        return esService.buildContentIndex();
    }
}

