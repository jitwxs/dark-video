package jit.wxs.dv.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jit.wxs.dv.convert.ContentBOConvert;
import jit.wxs.dv.convert.ContentVOConvert;
import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.domain.entity.DvContentAffix;
import jit.wxs.dv.domain.enums.CategoryLevelEnum;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ContentVO;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.handler.CustomException;
import jit.wxs.dv.service.DvContentAffixService;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内容Controller
 * @author jitwxs
 * @since 2018/10/4 14:59
 */
@Api(tags = {"内容管理"})
@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private DvContentService contentService;
    @Autowired
    private DvContentAffixService contentAffixService;
    @Autowired
    private ContentBOConvert contentBOConvert;
    @Autowired
    private ContentVOConvert contentVOConvert;

    @ApiOperation(value= "获取目录下最新内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name="category", value="分类ID", required = true),
            @ApiImplicitParam(paramType = "query", name="level", value="目录级别", required = true),
            @ApiImplicitParam(paramType = "query", name="count", value="内容条数，默认为8")
    })
    @GetMapping(value = "/{category}/latest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO<List<ContentVO>> listLatestContent(@PathVariable String category, Integer level,
                                                       @RequestParam(defaultValue = "8") Integer count) throws CustomException {
        CategoryLevelEnum levelEnum = CategoryLevelEnum.getEnum(level);
        if(levelEnum == null) {
            throw new CustomException(ResultEnum.PARAM_ERROR.getCode(), "目录级别不合法");
        }

        return contentService.listLatestContent(levelEnum, category, count);
    }

    @ApiOperation(value= "获取目录下最热内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name="category", value="分类ID", required = true),
            @ApiImplicitParam(paramType = "query", name="level", value="目录级别", required = true),
            @ApiImplicitParam(paramType = "query", name="count", value="内容条数，默认为8")
    })
    @GetMapping(value = "/{category}/hot", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO<List<ContentVO>> listHotContent(@PathVariable String category,
                                   Integer level,
                                   @RequestParam(defaultValue = "8") Integer count) throws CustomException {
        CategoryLevelEnum levelEnum = CategoryLevelEnum.getEnum(level);
        if(levelEnum == null) {
            throw new CustomException(ResultEnum.PARAM_ERROR.getCode(), "目录级别不合法");
        }

        return contentService.listHotContent(levelEnum, category, count);
    }

    @ApiOperation(value= "读取附件信息")
    @ApiImplicitParam(paramType = "path", name="suffix", value="附件ID", required = true)
    @GetMapping(value = "/suffix/{suffix}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVO getContentSuffix(@PathVariable String suffix) {
        DvContentAffix contentAffix = contentAffixService.selectById(suffix);

        if("video".equals(contentAffix.getType())) {
            return ResultVOUtils.success(contentBOConvert.convertAffix2Video(contentAffix, null, null));
        } else {
            return ResultVOUtils.success(contentBOConvert.convertAffix2BO(contentAffix, null, null));
        }
    }

    @ApiOperation(value= "读取内容列表")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map listContent(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "recPerPage", defaultValue = "10") Integer recPerPage,
                           @RequestParam(value = "order", defaultValue = "asc") String order,
                           @RequestParam(value = "sortBy", defaultValue = "create_date") String sortBy,
                           @RequestParam(value = "search", required = false) String search) {
        EntityWrapper<DvContent> wrapper = new EntityWrapper<>();
        if(StringUtils.isNotBlank(search)) {
            wrapper.like("name", search);
        }

        Page<DvContent> selectPage = contentService.selectPage(new Page<>(page, recPerPage, sortBy, "asc".equals(order)), wrapper);

        /*
        {
            // 数据请求结果（必须），当值为 'succes'、'ok' 或 200 时则是为请求成功
            "result": "success",

            // 远程数据内容（必须）
            "data": [
                // ... 原创数据对象数组
            ],

            // 用户界面提示消息，当请求结果失败时，可以使用此属性返回文本显示在用户界面上
            "message": "",

            // 远程数据的分页信息对象（必须），其中
            "pager": {
                "page": 1,           // 当前数据对应的页码
                "recTotal": 1001,    // 总的数据数目
                "recPerPage": 10,    // 每页数据数目
            }
        }
         */
        Map<String, Object> map = new HashMap<>(16);
        map.put("result", 200);
        map.put("data", contentVOConvert.convert(selectPage.getRecords()));
        map.put("message", "加载完毕");
        Map<String, Object> pager = new HashMap<>(16);
        pager.put("page", selectPage.getCurrent());
        pager.put("recTotal", selectPage.getTotal());
        pager.put("recPerPage", selectPage.getSize());
        map.put("pager",pager);

        return map;
    }

}
