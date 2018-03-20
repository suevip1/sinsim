package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.model.abnormal_record.AbnormalRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AbnormalRecordMapper extends Mapper<AbnormalRecord> {
    List<AbnormalRecordDetail> selectAbnormalRecordDetails(Integer taskRecordId);
    List<AbnormalRecordDetail> selectAbnormalRecordDetailList(@Param("abnormal_type")Integer abnormalType,
                                                                     @Param("task_name")String taskName,
                                                                     @Param("submit_user")Integer submitUser,
                                                                     @Param("solution_user")Integer solutionUser,
                                                                     @Param("query_start_time")Date queryStartTime,
                                                                     @Param("query_finish_time")Date queryFinishTime);
}