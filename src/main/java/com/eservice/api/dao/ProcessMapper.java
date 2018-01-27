package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.process.Process;

import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProcessMapper extends Mapper<Process> {

    List<Process> selectProcess(@Param("id") Integer id);

    List<Process> selectProcessSimple(@Param("id") Integer id);

}