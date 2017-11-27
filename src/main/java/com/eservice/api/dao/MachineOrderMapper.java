package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.model.user.UserDetail;
import org.apache.ibatis.annotations.Param;

public interface MachineOrderMapper extends Mapper<MachineOrder> {

    MachineOrderDetail getOrderAllDetail(@Param("id") Integer id);
}