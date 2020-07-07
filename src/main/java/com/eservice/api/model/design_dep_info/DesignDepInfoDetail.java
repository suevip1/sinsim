package com.eservice.api.model.design_dep_info;

import javax.persistence.*;
import java.util.Date;

@Table(name = "design_dep_info")
public class DesignDepInfoDetail extends DesignDepInfo {

    //订单审核 的更新时间
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}