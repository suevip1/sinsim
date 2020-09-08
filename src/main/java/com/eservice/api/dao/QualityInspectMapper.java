package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.quality_inspect.QualityInspect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityInspectMapper extends Mapper<QualityInspect> {
    List<QualityInspect> getQualityInspectByTaskName(@Param("taskName")String taskName);
}