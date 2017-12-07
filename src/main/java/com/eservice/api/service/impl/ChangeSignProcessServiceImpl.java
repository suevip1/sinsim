package com.eservice.api.service.impl;

import com.eservice.api.dao.ChangeSignProcessMapper;
import com.eservice.api.model.change_sign_process.ChangeSignProcess;
import com.eservice.api.service.ChangeSignProcessService;
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
public class ChangeSignProcessServiceImpl extends AbstractService<ChangeSignProcess> implements ChangeSignProcessService {
    @Resource
    private ChangeSignProcessMapper changeSignProcessMapper;

}
