package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.install_plan.InstallPlan;

import java.util.List;

public interface InstallPlanMapper extends Mapper<InstallPlan> {
    List<InstallPlan> selectUnSendInstallPlans();
}