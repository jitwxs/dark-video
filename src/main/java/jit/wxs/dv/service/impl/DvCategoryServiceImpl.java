package jit.wxs.dv.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.domain.entity.DvCategory;
import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.mapper.DvCategoryMapper;
import jit.wxs.dv.mapper.DvContentMapper;
import jit.wxs.dv.service.DvCategoryService;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.util.FileUtils;
import jit.wxs.dv.util.ResultVOUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 类别表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-10-04
 */
@Service
public class DvCategoryServiceImpl extends ServiceImpl<DvCategoryMapper, DvCategory> implements DvCategoryService {
    @Autowired
    private DvCategoryMapper categoryMapper;
    @Autowired
    private DvContentMapper contentMapper;
    @Autowired
    private DvContentService contentService;

    @Override
    public ResultVO genCategory(String rootPath) {
        // 在开始操作前，清理掉即所属目录为Null的内容
        contentMapper.delete(new EntityWrapper<DvContent>().isNull("category"));

        // 获取所有存在目录的ID，用于筛选掉失效目录
        List<String> ids = categoryMapper.listIds();

        // 插入目录
        insertDir(rootPath, ids, rootPath, null);

        // 清除失效目录
        for(String id : ids) {
            // 清除目录下内容
            contentService.delete(new EntityWrapper<DvContent>().eq("category", id));
            // 清除目录
            categoryMapper.deleteById(id);
        }

        return ResultVOUtils.success();
    }

    /**
     * 初始化目录
     *
     * @param rootPath 根目录
     * @param ids      数据库已经存在的目录Id集合，用于判断失效目录
     * @param path     目录路径
     * @param parentId 父目录路径
     * @author jitwxs
     * @since 2018/8/25 3:44
     */
    private void insertDir(String rootPath, List<String> ids, String path, String parentId) {
        // 获取所有子目录
        List<File> fileList = FileUtils.listDir(path);

        for (File file : fileList) {
            // 只查看Content目录下内容
            if (!file.getPath().contains("Content")) {
                continue;
            }

            // 如果目录以_files结尾，代表为网页的资源文件，忽略掉
            if (file.getPath().endsWith("_files")) {
                continue;
            }

            DvCategory category = new DvCategory();

            // 得到url的MD5值作为ID
            String url = file.getPath().replace(rootPath,"");
            String id = DigestUtils.md5Hex(url);

            // 判断目录是否存在，如果不存在，创建目录
            if (categoryMapper.selectById(id) == null) {
                category.setId(id);
                category.setName(file.getName());
                category.setPath(url);
                category.setParentId(parentId);
                category.setCreateDate(new Date());
                // 插入目录
                categoryMapper.insert(category);
            } else {
                // 如果存在，从集合中移除
                ids.remove(id);
            }

            // 插入内容
            contentService.genContent(file.getPath(), url, id);
            insertDir(rootPath, ids, file.getPath(), id);
        }
    }
}
