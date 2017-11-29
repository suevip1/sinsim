package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.model.user.UserDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MachineOrderMapper extends Mapper<MachineOrder> {

    MachineOrderDetail getOrderAllDetail(@Param("id") Integer id);
    List<MachineOrderDetail> selectOrderFuzzy(@Param("id") Integer id,
                                               @Param("contract_num") String contract_num,
                                               @Param("status") Integer status,
                                               @Param("sellman") String sellman,
                                               @Param("customer") String customer,
                                               @Param("query_start_time") String query_start_time,
                                               @Param("query_finish_time") String query_finish_time,
                                               @Param("machine_name") String machine_name);
    MachineOrderDetail selectOrder();
}