package com.eservice.api.service.impl;

import com.eservice.api.dao.ProcessRecordMapper;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.service.ProcessRecordService;
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
public class ProcessRecordServiceImpl extends AbstractService<ProcessRecord> implements ProcessRecordService {
    @Resource
    private ProcessRecordMapper processRecordMapper;

}
