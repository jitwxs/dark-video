package jit.wxs.dv.service;


import com.baomidou.mybatisplus.service.IService;
import jit.wxs.dv.domain.entity.DvContent;

/**
 * <p>
 * 内容表 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
public interface DvContentService extends IService<DvContent> {

    /**
     * 生成内容
     * @author jitwxs
     * @since 2018/10/4 2:42
     */
    void genContent(String path, String url, String parentId);
}
