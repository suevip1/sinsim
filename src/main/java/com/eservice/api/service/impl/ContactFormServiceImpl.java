package com.eservice.api.service.impl;

import com.eservice.api.dao.ContactFormMapper;
import com.eservice.api.model.change_item.ChangeItem;
import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.model.contact_form.ContactFormAllInfo;
import com.eservice.api.model.contact_form.ContactFormDetail;
import com.eservice.api.model.contact_fulfill.ContactFulfill;
import com.eservice.api.model.contact_sign.ContactSign;
import com.eservice.api.service.ContactFormService;
import com.eservice.api.core.AbstractService;
import com.eservice.api.service.common.Constant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


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

    @Resource
    private ContactFormServiceImpl contactFormService;

    @Resource
    private ContactSignServiceImpl contactSignService;

    @Resource
    private ContactFulfillServiceImpl contactFulfillService;

    @Resource
    private ChangeItemServiceImpl changeItemService;
    private Logger logger = Logger.getLogger(ContactFormServiceImpl.class);
    public List<ContactFormDetail> selectContacts(String contactType,
                                                  String orderNum,
                                                  String applicantDepartment,
                                                  String applicantPerson,
                                                  String userRoleName,
                                                  String strStatus,
                                                  String queryStartTime,
                                                  String queryFinishTime,
                                                  String currentStep,
                                                  Boolean isFuzzy){
        return contactFormMapper.selectContacts(contactType,
                orderNum,
                applicantDepartment,
                applicantPerson,
                userRoleName,
                strStatus,
                queryStartTime,
                queryFinishTime,
                currentStep,
                isFuzzy);
    }

    public void saveAndGetID(ContactForm contactForm ){
        contactFormMapper.saveAndGetID(contactForm);
    }

    public List<ContactForm> getLxdLastSerialNumber(String year, String department){
        return contactFormMapper.getLxdLastSerialNumber(year, department);
    }

    public ContactFormAllInfo getAllInfo(Integer contactFormId){

        ContactFormAllInfo contactFormAllInfo = new ContactFormAllInfo();

        ContactForm cf = contactFormService.findById(contactFormId);
        if(null == cf){
            logger.warn("根据该contactFormId" + contactFormId + " 找不到对应的contactForm");
            return null;
        }

        ContactSign cs = contactSignService.getContactSignByLxdId( contactFormId );////
        if(null == cs){
            logger.warn("根据该contactFormId" + contactFormId + " 找不到对应的contactSign");
            return null;
        }

        //落实单
        ContactFulfill cff = contactFulfillService.getFulFillByLxdId(contactFormId);
        if(null == cff){
            logger.warn("根据该contactFormId" + contactFormId + " 找不到对应的落实单");
        }
        contactFormAllInfo.setContactForm(cf);
        contactFormAllInfo.setContactSign(cs);
        contactFormAllInfo.setContactFulfill(cff);
        if(cf.getContactType().equals(Constant.STR_LXD_TYPE_BIANGENG)) {
            List<ChangeItem> changeItemList = changeItemService.selectChangeItemList(contactFormId);
            contactFormAllInfo.setChangeItemList(changeItemList);
        }

        return contactFormAllInfo;
    }
}
