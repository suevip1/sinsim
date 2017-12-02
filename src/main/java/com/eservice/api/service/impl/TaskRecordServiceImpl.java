package com.eservice.api.service.impl;

import com.eservice.api.dao.TaskRecordMapper;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.service.TaskRecordService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/01.
*/
@Service
@Transactional
public class TaskRecordServiceImpl extends AbstractService<TaskRecord> implements TaskRecordService {
    @Resource
    private TaskRecordMapper taskRecordMapper;

}
