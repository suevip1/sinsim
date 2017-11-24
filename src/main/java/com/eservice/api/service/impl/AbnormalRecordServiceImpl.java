package com.eservice.api.service.impl;

import com.eservice.api.dao.AbnormalRecordMapper;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.service.AbnormalRecordService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/14.
*/
@Service
@Transactional
public class AbnormalRecordServiceImpl extends AbstractService<AbnormalRecord> implements AbnormalRecordService {
    @Resource
    private AbnormalRecordMapper abnormalRecordMapper;

}
