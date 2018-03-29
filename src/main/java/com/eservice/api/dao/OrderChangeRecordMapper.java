package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.order_change_record.OrderChangeRecordView;
import com.eservice.api.model.order_change_record.OrderChangeRecord;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderChangeRecordMapper extends Mapper<OrderChangeRecord> {

    List<OrderChangeRecordView> getChangeRecordList(@Param("id") Integer id, @Param("orderId") Integer orderId);
}