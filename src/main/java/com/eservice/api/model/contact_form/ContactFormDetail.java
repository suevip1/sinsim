package com.eservice.api.model.contact_form;


import javax.persistence.*;
@Table(name = "contact_form")
public class ContactFormDetail extends ContactForm{

    /**
     * 对应的订单号
     */
    private String orderNum;

     /**
     * 联系单的当前签核步骤
     */
    @Column(name = "current_step")
    private String currentStep;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }


    /**
     * 获取联系单的当前签核步骤
     *
     * @return current_step - 联系单的当前签核步骤
     */
    public String getCurrentStep() {
        return currentStep;
    }

    /**
     * 设置联系单的当前签核步骤
     *
     * @param currentStep 联系单的当前签核步骤
     */
    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }
}