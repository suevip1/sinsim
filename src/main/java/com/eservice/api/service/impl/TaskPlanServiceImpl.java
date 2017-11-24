package com.eservice.api.service.impl;

import com.eservice.api.dao.TaskPlanMapper;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.service.TaskPlanService;
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
public class TaskPlanServiceImpl extends AbstractService<TaskPlan> implements TaskPlanService {
    @Resource
    private TaskPlanMapper taskPlanMapper;

}
