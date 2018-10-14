package jit.wxs.dv.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import jit.wxs.dv.domain.entity.DvContentLookHistory;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 观看历史表 Mapper 接口
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-14
 */
public interface DvContentLookHistoryMapper extends BaseMapper<DvContentLookHistory> {

    DvContentLookHistory selectByContentIdAndUserName(@Param("contentId") String contentId, @Param("username") String username);
}
