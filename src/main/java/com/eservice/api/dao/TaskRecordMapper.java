package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskRecordMapper extends Mapper<TaskRecord> {
    List<TaskRecord> selectTaskReocords(@Param("userAccount") String userAccount);
    List<TaskPlan> selectTaskPlans(@Param("taskRecordId") Integer taskRecordId);
    TaskRecordDetail selectTaskRecordDetail(@Param("taskRecordId") Integer taskRecordId);
    List<TaskRecordDetail> selectAllTaskRecordDetail();
    List<TaskRecordDetail> selectAllInstallTaskRecordDetailByUserAccount(@Param("userAccount") String userAccount);
    List<TaskRecordDetail> selectAllQaTaskRecordDetailByUserAccount(@Param("userAccount") String userAccount);
    List<TaskRecord> selectNotPlanedTaskRecord(@Param("process_record_id")Integer processRecordId);
	List<TaskRecord> getTaskRecordData(@Param("id") Integer id, @Param("processRecordId") Integer processRecordId);
    List<TaskRecordDetail> selectTaskRecordByMachineNameplate(@Param("namePlate") String namePlate);
    int deleteTaskRecordByCondition(@Param("id") Integer id, @Param("processRecordId") Integer processRecordId);
    List<TaskRecordDetail> selectPlanedTaskRecordsByFuzzy( @Param("order_num") String orderNum,
                                                           @Param("machine_strid") String machineStrId,
                                                           @Param("task_name") String taskName,
                                                           @Param("nameplate") String nameplate,
                                                           @Param("install_status") Integer installStatus,
                                                           @Param("machine_type") Integer machineType,
                                                           @Param("query_start_time") String query_start_time,
                                                           @Param("query_finish_time") String query_finish_time);
    List<TaskRecordDetail> selectPlanedTaskRecords(@Param("order_num") String orderNum,
                                                   @Param("machine_strid") String machineStrId,
                                                   @Param("task_name") String taskName,
                                                   @Param("nameplate") String nameplate,
                                                   @Param("install_status") Integer installStatus,
                                                   @Param("machine_type") Integer machineType,
                                                   @Param("query_start_time") String query_start_time,
                                                   @Param("query_finish_time") String query_finish_time);
}