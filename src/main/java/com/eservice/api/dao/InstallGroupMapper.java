package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.install_group.InstallGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InstallGroupMapper extends Mapper<InstallGroup> {
    @Select("SELECT * FROM install_group WHERE type = #{type}")
    List<InstallGroup> getInstallGroupByType(@Param("type")String type);

    @Select("SELECT * FROM\tinstall_group ig JOIN task ON ig.id = task.group_id WHERE task.task_name = #{TaskName}")
    InstallGroup getInstallGroupByTaskName(@Param("TaskName")String TaskName);

}