package com.eservice.api.model.contract;

import java.util.Date;
import javax.persistence.*;

public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 合同号
     */
    @Column(name = "contract_num")
    private String contractNum;

    /**
     * 客户姓名
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 销售人员
     */
    @Column(name = "sellman")
    private String sellman;

    /**
     * 合同备注，由填表人员填入
     */
    @Column(name="mark")
    private String mark;

    /**
     * 合同状态
     */
    @Column(name="status")
    private Byte status;

    /**
     * 合同支付方式
     */
    @Column(name="pay_method")
    private String payMethod;

    /**
     * 币种
     */
    @Column(name="currency_type")
    private String currencyType;

    /**
     * 销售组
     */
    @Column(name="market_group_name")
    private String marketGroupName;

    /**
     * 合同交货时间
     */
    @Column(name="contract_ship_date")
    private Date contractShipDate;

    /**
     * 填表时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    public String getRecordUser() {
        return recordUser;
    }

    public void setRecordUser(String recordUser) {
        this.recordUser = recordUser;
    }

    private String recordUser;


    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    private String isValid;


    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取合同号
     *
     * @return contract_num - 合同号
     */
    public String getContractNum() {
        return contractNum;
    }

    /**
     * 设置合同号
     *
     * @param contractNum 合同号
     */
    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    /**
     * 获取客户姓名
     *
     * @return customer_name - 客户姓名
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户姓名
     *
     * @param customerName 客户姓名
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取销售人员
     *
     * @return sellman - 销售人员
     */
    public String getSellman() {
        return sellman;
    }

    /**
     * 设置销售人员
     *
     * @param sellman 销售人员
     */
    public void setSellman(String sellman) {
        this.sellman = sellman;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getMarketGroupName() {
        return marketGroupName;
    }

    public void setMarketGroupName(String marketGroupName) {
        this.marketGroupName = marketGroupName;
    }

    public Date getContractShipDate() {
        return contractShipDate;
    }

    public void setContractShipDate(Date contractShipDate) {
        this.contractShipDate = contractShipDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取填表时间
     *
     * @return create_time - 填表时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置填表时间
     *
     * @param createTime 填表时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    //内贸部分区
    @Column(name = "domestic_trade_zone")
    private Integer domesticTradeZone;

    public Integer getDomesticTradeZone() {
        return domesticTradeZone;
    }

    public void setDomesticTradeZone(Integer domesticTradeZone) {
        this.domesticTradeZone = domesticTradeZone;
    }

}