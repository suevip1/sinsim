package com.eservice.api.service.impl;

import com.eservice.api.dao.InstallPlanAcutualMapper;
import com.eservice.api.model.install_plan_acutual.InstallPlanAcutual;
import com.eservice.api.service.InstallPlanAcutualService;
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
public class InstallPlanAcutualServiceImpl extends AbstractService<InstallPlanAcutual> implements InstallPlanAcutualService {
    @Resource
    private InstallPlanAcutualMapper installPlanAcutualMapper;

}
