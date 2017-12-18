package com.eservice.api.service.impl;

import com.eservice.api.dao.TaskMapper;
import com.eservice.api.model.task.Task;
import com.eservice.api.service.TaskService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/18.
*/
@Service
@Transactional
public class TaskServiceImpl extends AbstractService<Task> implements TaskService {
    @Resource
    private TaskMapper taskMapper;

}
