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
}