package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MachineOrderMapper extends Mapper<MachineOrder> {

    MachineOrderDetail getOrderAllDetail(@Param("id") Integer id);
    List<MachineOrderDetail> selectOrderFuzzy(@Param("id") Integer id,
                                              @Param("contract_id") Integer contract_id,
                                              @Param("order_num") String order_num,
                                              @Param("contract_num") String contract_num,
                                              @Param("status") Integer status,
                                              @Param("sellman") String sellman,
                                              @Param("customer") String customer,
                                              @Param("market_group_name") String marketGroupName,
                                              @Param("query_start_time") String query_start_time,
                                              @Param("query_finish_time") String query_finish_time,
                                              @Param("queryStartTimeSign") String queryStartTimeSign,
                                              @Param("queryFinishTimeSign") String queryFinishTimeSign,
                                              @Param("machine_name") String machine_name);
    List<MachineOrderDetail>  selectOrder(@Param("id") Integer id,
                                          @Param("contract_id") Integer contract_id,
                                          @Param("order_num") String order_num,
                                          @Param("contract_num") String contract_num,
                                          @Param("status") Integer status,
                                          @Param("sellman") String sellman,
                                          @Param("customer") String customer,
                                          @Param("market_group_name") String marketGroupName,
                                          @Param("query_start_time") String query_start_time,
                                          @Param("query_finish_time") String query_finish_time,
                                          @Param("queryStartTimeSign") String queryStartTimeSign,
                                          @Param("queryFinishTimeSign") String queryFinishTimeSign,
                                          @Param("machine_name") String machine_name);

    void saveAndGetID(MachineOrder machineOrder);


    MachineOrder searchOrderIdByOrderLoadingListId(@Param("ollId") Integer ollId);

    List<Integer> getUsedMachineTypeCount(@Param("machineTypeId") Integer machineTypeId);

    MachineOrder getMachineOrder(@Param("orderNum") String orderNum);

    MachineOrder getMachineOrderByNameplate(@Param("nameplate") String nameplate);
}