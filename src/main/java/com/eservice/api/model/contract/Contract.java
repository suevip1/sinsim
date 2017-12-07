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
    private String sellman;

    /**
     * 填表时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
}