package com.eservice.api.service.impl;

import com.eservice.api.dao.TaskQualityRecordMapper;
import com.eservice.api.model.task_quality_record.TaskQualityRecord;
import com.eservice.api.service.TaskQualityRecordService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/24.
*/
@Service
@Transactional
public class TaskQualityRecordServiceImpl extends AbstractService<TaskQualityRecord> implements TaskQualityRecordService {
    @Resource
    private TaskQualityRecordMapper taskQualityRecordMapper;

}