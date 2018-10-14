package jit.wxs.dv.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import jit.wxs.dv.domain.entity.DvCategory;

import java.util.List;

/**
 * <p>
 * 类别表 Mapper 接口
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
public interface DvCategoryMapper extends BaseMapper<DvCategory> {
    /**
     * 获取ID列表
     * @author jitwxs
     * @since 2018/10/4 2:34
     */
    List<String> listIds();

    /**
     * 获取分类名
     * @author jitwxs
     * @since 2018/10/14 15:57
     */
    String getName(String id);
}
