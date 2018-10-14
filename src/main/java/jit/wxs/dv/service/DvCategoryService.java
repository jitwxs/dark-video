package jit.wxs.dv.service;


import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.DvCategory;
import jit.wxs.dv.domain.enums.CategoryLevelEnum;
import jit.wxs.dv.domain.vo.TreeVO;

import java.util.List;

/**
 * <p>
 * 类别表 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
public interface DvCategoryService extends IService<DvCategory> {

    /**
     * 生成目录
     * @author jitwxs
     * @since 2018/10/4 2:26
     */
    void genCategory(String resContent, String author, String sessionId);

    /**
     * 获取目录
     * @param parentId 父目录ID，为空获取顶级目录
     * @author jitwxs
     * @since 2018/10/4 13:52
     */
    List<DvCategory> listCategory(String parentId);

    /**
     * 获取展示在导航地方的目录【即1级2级目录】
     * @author jitwxs
     * @since 2018/10/4 13:59
     */
    List<TreeVO> listNavCategory();

    /**
     * 获取目录级别枚举
     * @author jitwxs
     * @since 2018/10/4 23:44
     */
    CategoryLevelEnum getLevelEnum(String id);

    /**
     * 获取分类名
     * @author jitwxs
     * @since 2018/10/14 15:56
     */
    String getName(String id);
}
