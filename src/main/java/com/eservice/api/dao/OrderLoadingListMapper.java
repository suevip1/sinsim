package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.order_loading_list.OrderLoadingList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderLoadingListMapper extends Mapper<OrderLoadingList> {

    List<OrderLoadingList>  selectFilePathByOrderId(@Param("contractId") Integer contractId);
}