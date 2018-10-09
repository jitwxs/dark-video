package jit.wxs.dv.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import jit.wxs.dv.domain.entity.DvContentAffix;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 内容附件【三级目录内所有内容】表 Mapper 接口
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
public interface DvContentAffixMapper extends BaseMapper<DvContentAffix> {

    List<String> listIdsByContentId(String contentId);

    /**
     * 设置描述
     * @author jitwxs
     * @since 2018/10/9 21:59
     */
    String getDesc(@Param("containerId") String containerId, @Param("limit") int limit);
}
