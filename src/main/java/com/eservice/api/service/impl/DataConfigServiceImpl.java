package com.eservice.api.service.impl;

import com.eservice.api.dao.DataConfigMapper;
import com.eservice.api.model.data_config.DataConfig;
import com.eservice.api.service.DataConfigService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/08/27.
*/
@Service
@Transactional
public class DataConfigServiceImpl extends AbstractService<DataConfig> implements DataConfigService {
    @Resource
    private DataConfigMapper dataConfigMapper;

}
