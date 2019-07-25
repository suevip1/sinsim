package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.install_plan_actual.InstallPlanActual;
import com.eservice.api.model.install_plan_actual.InstallPlanActualDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InstallPlanActualMapper extends Mapper<InstallPlanActual> {
    List<InstallPlanActualDetails> selectInstallPlanActualDetails(@Param("orderNum") String orderNum,
                                                                  @Param("nameplate") String nameplate,
                                                                  @Param("installGroupName") String installGroupName);
}