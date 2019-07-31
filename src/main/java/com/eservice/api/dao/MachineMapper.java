package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine.MachinePlan;
import com.eservice.api.model.machine.MachineInfo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MachineMapper extends Mapper<Machine> {

    Machine searchMachineByAbnormalRecordId(Integer abnormalRecordId);

    Machine searchMachineByTaskQualityRecordId(Integer taskQualityRecordId);

    List<Machine> selectMachines(@Param("id") Integer id, @Param("order_id") Integer order_id,
                                 @Param("orderNum") String orderNum,
                                 @Param("machine_strid") String machine_strid, @Param("nameplate") String nameplate,
                                 @Param("location") String location, @Param("status") Byte status, @Param("machine_type") Integer machine_type,
                                 @Param("query_start_time") String query_start_time, @Param("query_finish_time") String query_finish_time);

    List<Machine> selectMachinesFuzzy(@Param("id") Integer id, @Param("order_id") Integer order_id,
                                      @Param("orderNum") String orderNum,
                                      @Param("machine_strid") String machine_strid, @Param("nameplate") String nameplate,
                                      @Param("location") String location, @Param("status") Byte status, @Param("machine_type") Integer machine_type,
                                      @Param("query_start_time") String query_start_time, @Param("query_finish_time") String query_finish_time);

    List<MachinePlan> selectPlanningMachines(@Param("order_num") String orderNum, @Param("machine_strid") String machine_strid, @Param("nameplate") String nameplate,
                                             @Param("location") String location, @Param("status") Byte status, @Param("machine_type") Integer machineType, @Param("date_type") Integer DateType,
                                             @Param("query_start_time") String query_start_time, @Param("query_finish_time") String query_finish_time);

    List<MachinePlan> selectPlanningMachinesFuzzy(@Param("order_num") String orderNum, @Param("machine_strid") String machine_strid, @Param("nameplate") String nameplate,
                                                  @Param("location") String location, @Param("status") Byte status, @Param("machine_type") Integer machineType, @Param("date_type") Integer DateType,
                                                  @Param("query_start_time") String query_start_time, @Param("query_finish_time") String query_finish_time);

    //selectConfigMachine
    List<MachineInfo> selectConfigMachine(
            @Param("order_id") Integer order_id, @Param("order_num") String orderNum, @Param("contract_num") String contractNum, @Param("machine_strid") String machine_strid, @Param("nameplate") String nameplate,
            @Param("machineType") Integer machineType,@Param("location") String location, @Param("status") Byte status,
            @Param("query_start_time") String query_start_time, @Param("query_finish_time") String query_finish_time, @Param("configStatus") Integer configStatus);

    List<MachineInfo> selectConfigMachineFuzzy(
            @Param("order_id") Integer order_id, @Param("order_num") String orderNum, @Param("contract_num") String contractNum, @Param("machine_strid") String machine_strid, @Param("nameplate") String nameplate,
            @Param("machineType") Integer machineType,@Param("location") String location, @Param("status") Byte status,
            @Param("query_start_time") String query_start_time, @Param("query_finish_time") String query_finish_time, @Param("configStatus") Integer configStatus);


    //selectProcessMachine
    List<MachineInfo> selectProcessMachineFuzzy(
            @Param("order_id") Integer order_id, @Param("order_num") String orderNum, @Param("contract_num") String contractNum, @Param("machine_strid") String machine_strid, @Param("nameplate") String nameplate,
            @Param("location") String location,
            @Param("statusArr") String[] statusArr,
            @Param("query_start_time") String query_start_time, @Param("query_finish_time") String query_finish_time,
            @Param("taskNameList") String taskNameList//工序集合，逗号分隔，支持UI按多个工序查询
            );

    List<MachineInfo> selectProcessMachine(
            @Param("order_id") Integer order_id, @Param("order_num") String orderNum, @Param("contract_num") String contractNum, @Param("machine_strid") String machine_strid, @Param("nameplate") String nameplate,
            @Param("location") String location,
            @Param("statusArr") String[] statusArr,
            @Param("query_start_time") String query_start_time, @Param("query_finish_time") String query_finish_time,
            @Param("taskNameList") String taskNameList//工序集合，逗号分隔，支持UI按多个工序查询
            );

    Machine selectMachinesByNameplate(@Param("nameplate") String nameplate);

}