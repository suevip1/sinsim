package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.optimizeTest.OptimizeTest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OptimizeTestMapper extends Mapper<OptimizeTest> {

    List<OptimizeTest> selectOptimizeList(
            @Param("projectName") String projectName,
            @Param("optimizePart") String optimizePart,
            @Param("orderNum") String orderNum,
            @Param("queryStartTimeCreate") String queryStartTimeCreate,
            @Param("queryFinishTimeCreate") String queryFinishTimeCreate,
            @Param("machineType") String machineType,
            @Param("purpose") String purpose,
            @Param("owner") String owner,
            @Param("queryStartTimeUpdate") String queryStartTimeUpdate,
            @Param("queryFinishTimeUpdate") String queryFinishTimeUpdate);

    void saveAndGetID(OptimizeTest optimizeTest);

}
