package jit.wxs.dv.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import jit.wxs.dv.domain.entity.DvContent;

import java.util.List;

/**
 * <p>
 * 内容表 Mapper 接口
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
public interface DvContentMapper extends BaseMapper<DvContent> {

    /**
     * 获取某一目录下所有内容ID
     * @author jitwxs
     * @since 2018/8/25 3:31
     */
    List<String> listIdsByCategory(String parentId);
}
