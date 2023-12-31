package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.model.abnormal_record.AbnormalRecordDetail;
import com.eservice.api.model.abnormal_record.AbnormalRecordStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AbnormalRecordMapper extends Mapper<AbnormalRecord> {
    List<AbnormalRecordDetail> selectAbnormalRecordDetails(Integer taskRecordId);

    List<AbnormalRecordDetail> selectAbnormalRecordDetailList(@Param("nameplate") String nameplate,
                                                              @Param("orderNum") String orderNum,
                                                              @Param("abnormal_type") Integer abnormalType,
                                                              @Param("task_name") String taskName,
                                                              @Param("submit_user") Integer submitUser,
                                                              @Param("solution_user") Integer solutionUser,
                                                              @Param("finish_status") Integer finishStatus,
                                                              @Param("query_start_time") String queryStartTime,
                                                              @Param("query_finish_time") String queryFinishTime);

    void saveAndGetID(AbnormalRecord abnormalRecord);

    List<AbnormalRecordStatistics> getAbnormalStatistics(@Param("mode") Integer mode);

}