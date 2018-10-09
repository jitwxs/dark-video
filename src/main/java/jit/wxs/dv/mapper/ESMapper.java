package jit.wxs.dv.mapper;

import jit.wxs.dv.domain.entity.ESContent;

public interface ESMapper {
    ESContent getParentESContent(String contentId);
}
