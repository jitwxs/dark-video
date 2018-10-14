package jit.wxs.dv.service;

import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.dv.domain.entity.ESContent;
import jit.wxs.dv.domain.vo.ResultVO;
import org.elasticsearch.index.query.BoolQueryBuilder;

/**
 * @author jitwxs
 * @since 2018/10/9 21:33
 */
public interface ESService {

    /**
     * 判断内容索引是否存在
     * @author jitwxs
     * @since 2018/10/9 22:19
     */
    boolean hasContentIndexExist();

    /**
     * 初始化内容索引
     * @author jitwxs
     * @since 2018/10/9 22:20
     */
    ResultVO createContentIndex();

    /**
     * 建立内容索引
     * @author jitwxs
     * @since 2018/10/9 23:12
     */
    void buildContentIndex(String sessionId);

    /**
     * 清空内容索引
     * @author jitwxs
     * @since 2018/10/9 23:25
     */
    long cleanContentIndex(BoolQueryBuilder queryBuilder);

    /**
     * 搜索内容
     * @param current 当前页
     * @param size 每页容量
     * @author jitwxs
     * @since 2018/10/9 23:54
     */
    Page<ESContent> searchContent(String name, int current, int size);

    /**
     * 判断内容索引中记录是否存在
     * @param documentId 记录Id
     * @author jitwxs
     * @since 2018/10/10 22:38
     */
    boolean hasContentDocumentExist(String documentId);
}
