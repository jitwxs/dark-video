package jit.wxs.dv.service;


import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.DvContentLookHistory;

/**
 * <p>
 * 观看历史表 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-14
 */
public interface DvContentLookHistoryService extends IService<DvContentLookHistory> {

    void addHistory(String contentId, String username);

    DvContentLookHistory selectByContentIdAndUserName(String contentId, String username);
}
