package jit.wxs.dv.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jitwxs
 * @since 2018/6/16 21:06
 */
@Data
public class TreeVO {
    private String id;

    private String name;

    private List<TreeVO> children;

    public TreeVO(String id, String name) {
        this.id = id;
        this.name = name;
        this.children = null;
    }

    public void addChildren(TreeVO treeVO) {
        if(this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(treeVO);
    }

}
