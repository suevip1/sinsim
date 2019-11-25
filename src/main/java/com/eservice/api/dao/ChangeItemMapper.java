package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.change_item.ChangeItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChangeItemMapper extends Mapper<ChangeItem> {
    List<ChangeItem> selectChangeItemList(@Param("contactFormId") Integer contactFormId);
}