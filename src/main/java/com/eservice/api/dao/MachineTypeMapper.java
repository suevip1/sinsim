package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.machine_type.MachineType;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface MachineTypeMapper extends Mapper<MachineType> {
    List<MachineType> selectByName(@Param("name")  String name);
}