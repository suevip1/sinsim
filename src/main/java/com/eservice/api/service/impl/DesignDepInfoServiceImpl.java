package com.eservice.api.service.impl;

import com.eservice.api.dao.DesignDepInfoMapper;
import com.eservice.api.model.design_dep_info.DesignDepInfo;
import com.eservice.api.service.DesignDepInfoService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2020/06/07.
*/
@Service
@Transactional
public class DesignDepInfoServiceImpl extends AbstractService<DesignDepInfo> implements DesignDepInfoService {
    @Resource
    private DesignDepInfoMapper designDepInfoMapper;

}
