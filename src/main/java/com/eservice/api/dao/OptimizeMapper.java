package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.optimize.Optimize;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OptimizeMapper extends Mapper<Optimize> {

    List<Optimize> selectOptimizeList(
            @Param("projectName")String  projectName,
            @Param("optimizePart")String optimizePart,
            @Param("orderNum")String orderNum,
            @Param("queryStartTimeCreate")String queryStartTimeCreate,
            @Param("queryFinishTimeCreate")String queryFinishTimeCreate,
            @Param("machineType")String machineType,
            @Param("purpose")String purpose,
            @Param("owner")String owner,
            @Param("queryStartTimeUpdate")String queryStartTimeUpdate,
            @Param("queryFinishTimeUpdate")String queryFinishTimeUpdate);
}