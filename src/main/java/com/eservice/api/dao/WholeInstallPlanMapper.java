package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.whole_install_plan.WholeInstallPlan;

import java.util.List;

public interface WholeInstallPlanMapper extends Mapper<WholeInstallPlan> {
    List<WholeInstallPlan> selectUnSendWIPs();
}