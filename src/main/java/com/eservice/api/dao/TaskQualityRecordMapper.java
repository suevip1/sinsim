package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.task_quality_record.TaskQualityRecord;
import com.eservice.api.model.task_quality_record.TaskQualityRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskQualityRecordMapper extends Mapper<TaskQualityRecord> {
    public List<TaskQualityRecordDetail> selectTaskQualityRecordDetails(@Param("taskRecordId") Integer taskRecordId);
    public void saveAndGetID( TaskQualityRecord taskQualityRecord );
}