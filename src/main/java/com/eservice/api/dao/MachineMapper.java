package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine.MachinePlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MachineMapper extends Mapper<Machine> {

    Machine searchMachineByAbnormalRecordId(Integer abnormalRecordId);
    Machine searchMachineByTaskQualityRecordId(Integer taskQualityRecordId);

    List<Machine> selectMachines(@Param("id")Integer id, @Param("order_id")Integer order_id, @Param("machine_id")String machine_id, @Param("nameplate")String nameplate,
                                 @Param("location")String location, @Param("status")Byte status, @Param("machine_type")Integer machine_type,
                                 @Param("query_start_time")String query_start_time, @Param("query_finish_time")String query_finish_time);
    List<Machine> selectMachinesFuzzy(@Param("id")Integer id, @Param("order_id")Integer order_id, @Param("machine_id")String machine_id, @Param("nameplate")String nameplate,
                                      @Param("location")String location, @Param("status")Byte status, @Param("machine_type")Integer machine_type,
                                      @Param("query_start_time")String query_start_time, @Param("query_finish_time")String query_finish_time);

    List<MachinePlan> selectPlanningMachines(@Param("order_num")String orderNum, @Param("machine_id")String machineId, @Param("nameplate")String nameplate,
                                             @Param("location")String location, @Param("status")Byte status, @Param("machine_type")Integer machineType,@Param("date_type")Integer DateType,
                                             @Param("query_start_time")String query_start_time, @Param("query_finish_time")String query_finish_time);
    List<MachinePlan> selectPlanningMachinesFuzzy(@Param("order_num")String orderNum, @Param("machine_id")String machineId, @Param("nameplate")String nameplate,
                                                  @Param("location")String location, @Param("status")Byte status, @Param("machine_type")Integer machineType,@Param("date_type")Integer DateType,
                                                  @Param("query_start_time")String query_start_time, @Param("query_finish_time")String query_finish_time);
}