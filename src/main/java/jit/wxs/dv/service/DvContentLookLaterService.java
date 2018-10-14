package jit.wxs.dv.service;


import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.DvContentLookLater;
import jit.wxs.dv.domain.vo.ResultVO;

/**
 * <p>
 * 稍后再看表 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-07
 */
public interface DvContentLookLaterService extends IService<DvContentLookLater> {

    ResultVO addLookLater(String contentId, String username);

    ResultVO deleteLookLater(String id);

    boolean hasExist(String contentId, String username);
}
