package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.order_sign.OrderSign;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderSignMapper extends Mapper<OrderSign> {
    List<OrderSign> getOrderSignListByContractId(@Param("contractId") Integer contractId);
    List<OrderSign> getOrderSignListByOrderId(@Param("orderId") Integer orderId);
}