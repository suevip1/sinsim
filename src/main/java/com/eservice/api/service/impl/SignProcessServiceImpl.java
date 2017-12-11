package com.eservice.api.service.impl;

import com.eservice.api.dao.SignProcessMapper;
import com.eservice.api.model.sign_process.SignProcess;
import com.eservice.api.service.SignProcessService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/09.
*/
@Service
@Transactional
public class SignProcessServiceImpl extends AbstractService<SignProcess> implements SignProcessService {
    @Resource
    private SignProcessMapper signProcessMapper;

}
