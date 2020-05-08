package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.install_plan.InstallPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InstallPlanMapper extends Mapper<InstallPlan> {
    List<InstallPlan> selectUnSendInstallPlans();

    List<InstallPlan> getInstallPlanByMachineId(@Param("machineId")Integer machineId);
}