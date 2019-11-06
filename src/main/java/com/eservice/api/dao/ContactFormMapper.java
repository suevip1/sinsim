package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.contact_form.ContactForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContactFormMapper extends Mapper<ContactForm> {
    List<ContactForm> selectContacts(@Param("contactType")String contactType,
                                     @Param("contractNum")String contractNum,
                                     @Param("applicantDepartment")String applicantDepartment,
                                     @Param("applicantPerson")String applicantPerson,
                                     @Param("status")Integer status,
                                     @Param("queryStartTime")String queryStartTime,
                                     @Param("queryFinishTime")String queryFinishTime,
                                     @Param("isFuzzy")Boolean isFuzzy);
}