package jit.wxs.dv.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.domain.entity.DvContentLookHistory;
import jit.wxs.dv.mapper.DvContentLookHistoryMapper;
import jit.wxs.dv.service.DvContentLookHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 观看历史表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-14
 */
@Service
public class DvContentLookHistoryServiceImpl extends ServiceImpl<DvContentLookHistoryMapper, DvContentLookHistory> implements DvContentLookHistoryService {
    @Autowired
    private DvContentLookHistoryMapper lookHistoryMapper;

    @Override
    public void addHistory(String contentId, String username) {
        DvContentLookHistory lookHistory = selectByContentIdAndUserName(contentId, username);
        if(lookHistory == null) {
            lookHistory = new DvContentLookHistory(username, contentId);
            lookHistoryMapper.insert(lookHistory);
        } else {
            lookHistory.setLookDate(new Date());
            lookHistoryMapper.updateById(lookHistory);
        }
    }

    @Override
    public DvContentLookHistory selectByContentIdAndUserName(String contentId, String username) {
        return lookHistoryMapper.selectByContentIdAndUserName(contentId, username);
    }
}
