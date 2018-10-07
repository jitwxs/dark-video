package jit.wxs.dv.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.dv.domain.entity.DvCategory;
import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.domain.enums.CategoryLevelEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.domain.vo.TreeVO;
import jit.wxs.dv.mapper.DvCategoryMapper;
import jit.wxs.dv.mapper.DvContentMapper;
import jit.wxs.dv.service.DvCategoryService;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.util.FileUtils;
import jit.wxs.dv.util.ResultVOUtils;
import jit.wxs.dv.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
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
    public List<DvCategory> listCategory(String parentId) {
        List<DvCategory> selectList;

        if(StringUtils.isBlank(parentId)) {
             selectList = categoryMapper.selectList(new EntityWrapper<DvCategory>().isNull("parent_id"));
        } else {
            selectList = categoryMapper.selectList(new EntityWrapper<DvCategory>().eq("parent_id", parentId));
        }

        return selectList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TreeVO> listNavCategory() {
        List<TreeVO> topList = new LinkedList<>();

        List<DvCategory> firstCategories = listCategory(null);
        for(DvCategory category : firstCategories) {
            TreeVO treeVO = new TreeVO(category.getId(), category.getName());

            List<DvCategory> secondCategories = listCategory(category.getId());
            for(DvCategory second : secondCategories) {
                treeVO.addChildren(new TreeVO(second.getId(), second.getName()));
            }

            topList.add(treeVO);
        }

        return topList;
    }

    @Override
    public CategoryLevelEnum getLevelEnum(String id) {
        DvCategory category = categoryMapper.selectById(id);

        return StringUtils.isBlank(category.getParentId()) ? CategoryLevelEnum.FIRST : CategoryLevelEnum.SECOND;
    }

    @Override
    public ResultVO genCategory(String resContent) {
        // 1、在开始操作前，清理掉一级目录为Null的内容
        contentMapper.delete(new EntityWrapper<DvContent>().isNull("first_category"));

        // 2、获取所有存在目录的ID，用于筛选掉失效目录
        List<String> ids = categoryMapper.listIds();

        // 3、处理逻辑
        // 3.1 获取所有一级目录
        for (File file : FileUtils.listDir(resContent)) {
            // 如果目录以_files结尾，代表为网页的资源文件，忽略掉
            if (file.getPath().endsWith("_files")) {
                continue;
            }

            DvCategory category = new DvCategory();

            // 得到url的MD5值作为ID
            String url = file.getPath().replace(resContent,"");
            String id = DigestUtils.md5Hex(url);

            // 判断目录是否存在，如果不存在，保存数据库
            if (categoryMapper.selectById(id) == null) {
                category.setId(id);
                category.setName(file.getName());
                category.setParentId(null);
                category.setCreateDate(new Date());
                // 插入目录
                categoryMapper.insert(category);
            } else {
                // 如果存在，从集合中移除
                ids.remove(id);
            }
            // 插入一级目录下的内容
            contentService.genContent(file.getPath(), url, id, null, false);

            // 3.2 获取所有二级目录
            for(File file1 : FileUtils.listDir(file.getPath())) {
                // 如果目录以_files结尾，代表为网页的资源文件，忽略掉
                if (file1.getPath().endsWith("_files")) {
                    continue;
                }

                DvCategory category1 = new DvCategory();

                // 得到url的MD5值作为ID
                String url1 = file1.getPath().replace(resContent,"");
                String id1 = DigestUtils.md5Hex(url1);

                // 判断目录是否存在，如果不存在，保存数据库
                if (categoryMapper.selectById(id1) == null) {
                    category1.setId(id1);
                    category1.setName(file1.getName());
                    // 设置parentId为一级目录的ID
                    category1.setParentId(id);
                    category1.setCreateDate(new Date());
                    // 插入目录
                    categoryMapper.insert(category1);
                } else {
                    // 如果存在，从集合中移除
                    ids.remove(id1);
                }

                // 插入二级目录下的内容
                contentService.genContent(file1.getPath(), url1, id, id1, true);
            }

        }

        // 4、清除失效目录
        for(String id : ids) {
            // 清除内容
            contentMapper.delete(new EntityWrapper<DvContent>().eq("first_category", id));
            contentMapper.delete(new EntityWrapper<DvContent>().eq("second_category", id));
            // 清除目录
            categoryMapper.deleteById(id);
        }

        return ResultVOUtils.success();
    }
}
