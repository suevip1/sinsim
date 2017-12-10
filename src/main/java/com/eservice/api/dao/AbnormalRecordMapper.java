package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.model.abnormal_record.AbnormalRecordDetail;

import java.util.List;

public interface AbnormalRecordMapper extends Mapper<AbnormalRecord> {
    public List<AbnormalRecordDetail> selectAbnormalRecordDetails(Integer taskRecordId);
}