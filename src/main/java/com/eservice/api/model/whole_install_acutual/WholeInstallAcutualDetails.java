package com.eservice.api.model.whole_install_acutual;

import javax.persistence.*;
import java.util.Date;

@Table(name = "whole_install_acutual")
public class WholeInstallAcutualDetails extends WholeInstallAcutual {
    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 机器编号（铭牌）
     */
    private String nameplate;

    /**
     * 机器的位置
     */
    private String location;

    /**
     * 头数
     */
    private String headNum;

    /**
     * 计划的备注
     */
    private String cmtSend;

    private String groupName;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getNameplate() {
        return nameplate;
    }

    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeadNum() {
        return headNum;
    }

    public void setHeadNum(String headNum) {
        this.headNum = headNum;
    }

    public String getCmtSend() {
        return cmtSend;
    }

    public void setCmtSend(String cmtSend) {
        this.cmtSend = cmtSend;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}