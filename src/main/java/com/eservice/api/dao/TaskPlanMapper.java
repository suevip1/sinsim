package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.task_plan.TaskPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskPlanMapper extends Mapper<TaskPlan> {
    List<TaskPlan> selectByUserAccount(@Param("userAccount")String userAccount);
}