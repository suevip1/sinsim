package com.eservice.api.model.contact_form;

import com.eservice.api.model.change_item.ChangeItem;
import com.eservice.api.model.contact_fulfill.ContactFulfill;
import com.eservice.api.model.contact_sign.ContactSign;

import javax.persistence.Table;
import java.util.List;

@Table(name = "contact_form")
public class ContactFormAllInfo {

    private ContactForm contactForm;

    public ContactForm getContactForm() {
        return contactForm;
    }

    public void setContactForm(ContactForm contactForm) {
        this.contactForm = contactForm;
    }

//    /**
//     * 对应的订单号
//     */
//    private String orderNum;
//
//    public String getOrderNum() {
//        return orderNum;
//    }
//
//    public void setOrderNum(String orderNum) {
//        this.orderNum = orderNum;
//    }

    /**
     * 变更项列表，类型为变更联系单时。
     */
    private List<ChangeItem> changeItemList;

    public List<ChangeItem> getChangeItemList() {
        return changeItemList;
    }

    public void setChangeItemList(List<ChangeItem> changeItemList) {
        this.changeItemList = changeItemList;
    }

    /**
     * 签核信息
     */
    private ContactSign contactSign;

    public ContactSign getContactSign() {
        return contactSign;
    }

    public void setContactSign(ContactSign contactSign) {
        this.contactSign = contactSign;
    }

    private String attachedFile;

    public String getAttachedFile() {
        return attachedFile;
    }

    public void setAttachedFile(String attachedFile) {
        this.attachedFile = attachedFile;
    }

    /**
     * 落实单
     */
    private ContactFulfill contactFulfill;

    public ContactFulfill getContactFulfill() {
        return contactFulfill;
    }

    public void setContactFulfill(ContactFulfill contactFulfill) {
        this.contactFulfill = contactFulfill;
    }
}