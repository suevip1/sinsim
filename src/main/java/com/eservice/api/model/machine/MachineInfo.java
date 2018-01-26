package com.eservice.api.model.machine;

import java.util.Date;
import javax.persistence.*;

public class MachineInfo extends Machine {

    /**
     * 订单编号
     */
    private String orderNum;


    /**
     * 合同号对应ID
     */
    private Integer contractId;


    /**
     * 合同编号
     */
    private String contractNum;

    /*
    流程进度ID
    标识是否已配置过流程
     */
    private String processRecordId;

    /*
    gets sets orderNum
     */
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }


    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    //processRecordId
    public String getProcessRecordId() {
        return processRecordId;
    }

    public void setProcessRecordId(String processRecordId) {
        this.processRecordId = processRecordId;
    }
}
