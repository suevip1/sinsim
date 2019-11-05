package com.eservice.api.service.impl;

import com.eservice.api.dao.ContactFormMapper;
import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.service.ContactFormService;
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
public class ContactFormServiceImpl extends AbstractService<ContactForm> implements ContactFormService {
    @Resource
    private ContactFormMapper contactFormMapper;

}
