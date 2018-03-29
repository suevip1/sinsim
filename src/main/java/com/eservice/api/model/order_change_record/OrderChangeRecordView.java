package com.eservice.api.model.order_change_record;

import javax.persistence.*;
import java.util.Date;

@Table(name = "order_change_record")
public class OrderChangeRecordView extends OrderChangeRecord {
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户姓名
     */
    private String userName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

}