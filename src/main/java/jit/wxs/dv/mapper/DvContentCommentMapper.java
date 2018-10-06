package jit.wxs.dv.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import jit.wxs.dv.domain.entity.DvContentComment;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 内容评论表 Mapper 接口
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-05
 */
public interface DvContentCommentMapper extends BaseMapper<DvContentComment> {

    int countByContentId(String contentId);

    boolean hasBelong(@Param("id") String id, @Param("username") String username);
}
