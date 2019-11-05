package com.eservice.api.service.impl;

import com.eservice.api.dao.ContactSignMapper;
import com.eservice.api.model.contact_sign.ContactSign;
import com.eservice.api.service.ContactSignService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/11/05.
*/
@Service
@Transactional
public class ContactSignServiceImpl extends AbstractService<ContactSign> implements ContactSignService {
    @Resource
    private ContactSignMapper contactSignMapper;

}
