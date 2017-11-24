package com.eservice.api.service.impl;

import com.eservice.api.dao.AbnormalMapper;
import com.eservice.api.model.abnormal.Abnormal;
import com.eservice.api.service.AbnormalService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/14.
*/
@Service
@Transactional
public class AbnormalServiceImpl extends AbstractService<Abnormal> implements AbnormalService {
    @Resource
    private AbnormalMapper abnormalMapper;

}
