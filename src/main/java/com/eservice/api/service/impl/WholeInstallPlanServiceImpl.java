package com.eservice.api.service.impl;

import com.eservice.api.dao.WholeInstallPlanMapper;
import com.eservice.api.model.whole_install_plan.WholeInstallPlan;
import com.eservice.api.service.WholeInstallPlanService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/06/22.
*/
@Service
@Transactional
public class WholeInstallPlanServiceImpl extends AbstractService<WholeInstallPlan> implements WholeInstallPlanService {
    @Resource
    private WholeInstallPlanMapper wholeInstallPlanMapper;

}
