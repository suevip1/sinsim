package com.eservice.api.model.contact_form;

import javax.persistence.*;
import java.util.Date;

@Table(name = "contact_form")
public class ContactFormDetail extends ContactForm{

    /**
     * 对应的订单号
     */
    private String orderNum;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}