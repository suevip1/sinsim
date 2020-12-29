package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.quality_inspect.QualityInspect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityInspectMapper extends Mapper<QualityInspect> {
    List<QualityInspect> getQualityInspectByTaskName(@Param("taskName")String taskName);
    List<QualityInspect> getQualityInspect(@Param("taskName")String taskName,
                                           @Param("inspectName")String inspectName,
                                           @Param("inspectType")String inspectType,
                                           @Param("inspectPhase")String inspectPhase,
                                           @Param("inspectContent")String inspectContent,
                                           @Param("isValid")Byte isValid);
}