package jit.wxs.dv.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import jit.wxs.dv.domain.entity.DvContent;
import org.apache.ibatis.annotations.Param;

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

    boolean hasExist(String id);

    /**
     * 读取一级目录下内容
     * @author jitwxs
     * @since 2018/10/4 15:26
     */
    List<String> listIdsByFirstCategory(String firstCategory);

    /**
     * 分页查询某一分类下内容
     * @param column 类别字段名
     * @param category 类别ID
     * @author jitwxs
     * @since 2018/10/4 23:38
     */
    List<DvContent> pageByCategory(@Param("column") String column,@Param("category") String category, Pagination pagination);

    void setThumbnail(@Param("thumbnail") String thumbnail, @Param("contentId") String contentId);

    /**
     * 设置点击量
     * @author jitwxs
     * @since 2018/10/14 17:58
     */
    void click(@Param("contentId") String contentId,@Param("click")  Integer click);
}
