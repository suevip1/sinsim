package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.task.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskMapper extends Mapper<Task> {
    List<Task> getTaskByNameplate(@Param("nameplate")String  nameplate);
}