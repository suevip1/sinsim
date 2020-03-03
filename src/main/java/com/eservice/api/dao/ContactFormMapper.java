package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.model.contact_form.ContactFormAllInfo;
import com.eservice.api.model.contact_form.ContactFormDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContactFormMapper extends Mapper<ContactForm> {
    List<ContactFormDetail> selectContacts(@Param("contactType")String contactType,
                                           @Param("orderNum")String orderNum,
                                           @Param("applicantDepartment")String applicantDepartment,
                                           @Param("applicantPerson")String applicantPerson,
                                           @Param("userRoleName")String userRoleName,
                                           @Param("strStatus")String strStatus,
                                           @Param("queryStartTime")String queryStartTime,
                                           @Param("queryFinishTime")String queryFinishTime,
                                           @Param("currentStep")String currentStep,
                                           @Param("isFuzzy")Boolean isFuzzy);
    void saveAndGetID(  ContactForm contactForm);
    List<ContactForm> getLxdLastSerialNumber( @Param("year")String year,
                                              @Param("department")String department);
//    ContactFormAllInfo getAllInfo(@Param("contactFormId") Integer contactFormId);
}