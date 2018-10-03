package jit.wxs.dv.service.impl;

import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.mapper.DvContentMapper;
import jit.wxs.dv.service.SysSettingService;
import jit.wxs.dv.service.ThumbnailService;
import jit.wxs.dv.util.FileUtils;
import jit.wxs.dv.util.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

/**
 * @author jitwxs
 * @since 2018/10/4 2:19
 */
@Service
public class ThumbnailServiceImpl implements ThumbnailService {
    @Autowired
    private DvContentMapper contentMapper;
    @Autowired
    private SysSettingService settingService;


    /**
     * 清理失效缩略图
     * @author jitwxs
     * @since 2018/10/4 2:20
     */
    @Override
    public ResultVO cleanThumbnail() {
        String resRoot = settingService.getResRoot();
        String thumbnailPath = resRoot + File.separator + "Thumbnail";

        // 递归查找缩略图根目录下所有jpg文件
        String[] selectSuffix = {"jpg"};
        Collection<File> listFiles = FileUtils.listFiles(new File(thumbnailPath), selectSuffix, true);

        int count = 0;
        for (File file : listFiles) {
            String id = FileUtils.getPrefix(file.getName());
            if(contentMapper.selectById(id) == null) {
                FileUtils.deleteQuietly(file);
                count++;
            }
        }
        return ResultVOUtils.success(count);
    }
}
