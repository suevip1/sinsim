package com.eservice.api.service.impl;

import com.eservice.api.dao.OptimizeMapper;
import com.eservice.api.model.optimize.Optimize;
import com.eservice.api.service.OptimizeService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2020/07/07.
*/
@Service
@Transactional
public class OptimizeServiceImpl extends AbstractService<Optimize> implements OptimizeService {
    @Resource
    private OptimizeMapper optimizeMapper;

}
