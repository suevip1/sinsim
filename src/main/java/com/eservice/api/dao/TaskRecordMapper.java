package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskRecordMapper extends Mapper<TaskRecord> {
    List<TaskRecord> selectTaskReocords(@Param("userAccount") String userAccount);
    List<TaskRecord> selectTaskPlans(@Param("taskId") Integer taskRecordId);
    TaskRecordDetail selectTaskRecordDetail(@Param("taskId") Integer taskRecordId);
}