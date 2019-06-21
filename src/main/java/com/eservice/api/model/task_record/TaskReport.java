package com.eservice.api.model.task_record;

import javax.persistence.*;
import java.util.Date;

@Table(name = "task_record")
public class TaskReport extends TaskRecord {

    private String nameplate;

    private String orderNum;


    public String getNameplate() {
        return nameplate;
    }

    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}