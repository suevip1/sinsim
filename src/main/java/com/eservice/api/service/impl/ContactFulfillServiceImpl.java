package com.eservice.api.service.impl;

import com.eservice.api.dao.ContactFulfillMapper;
import com.eservice.api.model.contact_fulfill.ContactFulfill;
import com.eservice.api.model.contact_fulfill.ContactFulfillDetail;
import com.eservice.api.service.ContactFulfillService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2020/07/01.
*/
@Service
@Transactional
public class ContactFulfillServiceImpl extends AbstractService<ContactFulfill> implements ContactFulfillService {
    @Resource
    private ContactFulfillMapper contactFulfillMapper;

    public List<ContactFulfillDetail> selectFulfillList(
            String applicantPerson,
            String applicantDepartment,
            String orderNum,
            String queryStartTimeCreate,
            String queryFinishTimeCreate,
            String fulfillMan,
            String fulfillStatus,
            String contactFormNum,
            String queryStartTimeUpdate,
            String queryFinishTimeUpdate
    ){
        return contactFulfillMapper.selectFulfillList(
                applicantPerson,
                applicantDepartment,
                orderNum,
                queryStartTimeCreate,
                queryFinishTimeCreate,
                fulfillMan,
                fulfillStatus,
                contactFormNum,
                queryStartTimeUpdate,
                queryFinishTimeUpdate
        );
    }

}
