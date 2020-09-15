package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.contact_fulfill.ContactFulfill;
import com.eservice.api.model.contact_fulfill.ContactFulfillDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContactFulfillMapper extends Mapper<ContactFulfill> {

    List<ContactFulfillDetail> selectFulfillList(
         @Param("applicantPerson")String  applicantPerson,
         @Param("applicantDepartment")String applicantDepartment,
         @Param("orderNum")String orderNum,
         @Param("queryStartTimeCreate")String queryStartTimeCreate,
         @Param("queryFinishTimeCreate")String queryFinishTimeCreate,
         @Param("fulfillMan")String fulfillMan,
         @Param("fulfillStatus")String fulfillStatus,
         @Param("contactFormNum")String contactFormNum,
         @Param("queryStartTimeUpdate")String queryStartTimeUpdate,
         @Param("queryFinishTimeUpdate")String queryFinishTimeUpdate);

    ContactFulfill getLatestFulFillByLxdId(@Param("contactFormId")Integer  contactFormId);
}