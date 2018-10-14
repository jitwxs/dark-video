package jit.wxs.dv.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.domain.entity.DvContentLookLater;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.mapper.DvContentLookLaterMapper;
import jit.wxs.dv.service.DvContentLookLaterService;
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
public class DvContentLookLaterServiceImpl extends ServiceImpl<DvContentLookLaterMapper, DvContentLookLater> implements DvContentLookLaterService {
    @Autowired
    private DvContentLookLaterMapper lookLaterMapper;

    @Override
    public ResultVO addLookLater(String contentId, String username) {
        if(StringUtils.isBlank(username)) {
            return ResultVOUtils.error(ResultEnum.AUTHORITY_ERROR);
        }

        if(hasExist(contentId, username)) {
            return ResultVOUtils.error(ResultEnum.LOOK_LATER_ALREADY);
        }

        DvContentLookLater lookLater = new DvContentLookLater(username, contentId);
        Integer integer = lookLaterMapper.insert(lookLater);

        return integer == 1 ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.ADD_LOOK_LATER_ERROR);
    }

    @Override
    public ResultVO deleteLookLater(String id) {
        return lookLaterMapper.deleteById(id) == 1 ? ResultVOUtils.success() : ResultVOUtils.error(ResultEnum.DELETE_RECORD_ERROR);
    }

    @Override
    public boolean hasExist(String contentId, String username) {
        return lookLaterMapper.hasExist(contentId, username);
    }
}
