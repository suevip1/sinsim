package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.abnormal_record.AbnormalRecordDetail;
import com.eservice.api.model.task_quality_record.TaskQualityRecord;
import com.eservice.api.model.task_quality_record.TaskQualityRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskQualityRecordMapper extends Mapper<TaskQualityRecord> {
    public List<TaskQualityRecordDetail> selectTaskQualityRecordDetails(@Param("taskRecordId") Integer taskRecordId);
    public void saveAndGetID( TaskQualityRecord taskQualityRecord );
    List<TaskQualityRecordDetail> selectTaskQualityList(@Param("nameplate")String nameplate,
                                                              @Param("task_name")String taskName,
                                                              @Param("submit_user")Integer submitUser,
                                                              @Param("solution_user")Integer solutionUser,
                                                              @Param("finish_status")Integer finishStatus,
                                                              @Param("query_start_time")String queryStartTime,
                                                              @Param("query_finish_time")String queryFinishTime);

}