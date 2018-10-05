package jit.wxs.dv.service;


import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.DvContentAffix;

import java.io.File;
import java.util.List;

/**
 * <p>
 * 内容附件【三级目录内所有内容】表 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
public interface DvContentAffixService extends IService<DvContentAffix> {

    /**
     * 初始化附件内容
     * @author jitwxs
     * @since 2018/10/4 16:11
     */
    void genContentAffix(File rootFile, String contentId);

    /**
     * 获取指定内容的所有附件
     * @author jitwxs
     * @since 2018/10/5 10:48
     */
    List<DvContentAffix> listByContentId(String contentId);
}
