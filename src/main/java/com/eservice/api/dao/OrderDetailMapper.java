package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.order_detail.OrderDetail;
import org.apache.ibatis.annotations.Param;

public interface OrderDetailMapper extends Mapper<OrderDetail> {
    void saveAndGetID( OrderDetail orderDetail);
}