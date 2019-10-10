package com.eservice.api.model.domestic_trade_zone;

import javax.persistence.*;

@Table(name = "domestic_trade_zone")
public class DomesticTradeZoneDetail extends DomesticTradeZone{

    /**
     * 内贸区域负责人的账号
     */
    private String account;

    /**
     * 内贸区域负责人的姓名
     */
    private String name;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}