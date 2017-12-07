package com.eservice.api.service.impl;

import com.eservice.api.dao.NormalSignProcessMapper;
import com.eservice.api.model.normal_sign_process.NormalSignProcess;
import com.eservice.api.service.NormalSignProcessService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/07.
*/
@Service
@Transactional
public class NormalSignProcessServiceImpl extends AbstractService<NormalSignProcess> implements NormalSignProcessService {
    @Resource
    private NormalSignProcessMapper normalSignProcessMapper;

}
