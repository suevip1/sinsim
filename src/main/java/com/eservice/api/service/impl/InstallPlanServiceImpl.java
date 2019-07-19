package com.eservice.api.service.impl;

import com.eservice.api.dao.InstallPlanMapper;
import com.eservice.api.model.install_plan.InstallPlan;
import com.eservice.api.service.InstallPlanService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/07/19.
*/
@Service
@Transactional
public class InstallPlanServiceImpl extends AbstractService<InstallPlan> implements InstallPlanService {
    @Resource
    private InstallPlanMapper installPlanMapper;

}
