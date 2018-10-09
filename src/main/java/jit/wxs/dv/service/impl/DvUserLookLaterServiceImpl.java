package jit.wxs.dv.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.domain.entity.DvUserLookLater;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.mapper.DvUserLookLaterMapper;
import jit.wxs.dv.service.DvUserLookLaterService;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 稍后再看表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-07
 */
@Service
public class DvUserLookLaterServiceImpl extends ServiceImpl<DvUserLookLaterMapper, DvUserLookLater> implements DvUserLookLaterService {
    @Autowired
    private DvUserLookLaterMapper lookLaterMapper;

    @Override
    public ResultVO addLookLater(String contentId, String username) {
        if(StringUtils.isBlank(contentId)) {
            return ResultVOUtils.error(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isBlank(username)) {
            return ResultVOUtils.error(ResultEnum.AUTHORITY_ERROR);
        }

        if(lookLaterMapper.hasExist(contentId, username)) {
            return ResultVOUtils.error(ResultEnum.LOOK_LATER_ALERADY);
        }

        DvUserLookLater lookLater = new DvUserLookLater(username, contentId);
        Integer integer = lookLaterMapper.insert(lookLater);

        return integer == 1 ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.ADD_LOOK_LATER_ERROR);
    }

    @Override
    public ResultVO deleteLookLater(String id) {
        if(StringUtils.isBlank(id)) {
            return ResultVOUtils.error(ResultEnum.PARAM_ERROR);
        }

        Integer integer = lookLaterMapper.deleteById(id);

        return integer == 1 ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.DELETE_RECORD_ERROR);
    }
}
