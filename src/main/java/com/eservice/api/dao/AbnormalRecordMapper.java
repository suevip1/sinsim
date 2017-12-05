package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.model.abnormal_record.AbnormalRecordDetail;

public interface AbnormalRecordMapper extends Mapper<AbnormalRecord> {
    public AbnormalRecordDetail selectAbnormalRecordDetail(Integer taskRecordId);
}