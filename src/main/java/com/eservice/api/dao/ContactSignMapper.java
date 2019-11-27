package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.contact_sign.ContactSign;
import org.apache.ibatis.annotations.Param;

public interface ContactSignMapper extends Mapper<ContactSign> {
    ContactSign getContactSign(@Param("contactFormId")Integer contactFormId);
}