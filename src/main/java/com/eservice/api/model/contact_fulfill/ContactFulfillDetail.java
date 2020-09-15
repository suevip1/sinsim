package com.eservice.api.model.contact_fulfill;

import com.eservice.api.model.contact_form.ContactForm;

import javax.persistence.*;
import java.util.Date;

public class ContactFulfillDetail extends  ContactFulfill{

    //不需要一个个成员去加
    private ContactForm contactForm;

    public ContactForm getContactForm() {
        return contactForm;
    }

    public void setContactForm(ContactForm contactForm) {
        this.contactForm = contactForm;
    }
}