package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.order_split_record.OrderSplitRecord;
import com.eservice.api.model.order_split_record.OrderSplitRecordView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderSplitRecordMapper extends Mapper<OrderSplitRecord> {

    List<OrderSplitRecordView> getSplitRecordList(@Param("id") Integer id, @Param("orderId") Integer orderId);
}