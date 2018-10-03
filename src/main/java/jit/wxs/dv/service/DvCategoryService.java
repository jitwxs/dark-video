package jit.wxs.dv.service;


import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.DvCategory;
import jit.wxs.dv.domain.vo.ResultVO;

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
    ResultVO genCategory(String rootPath);
}
