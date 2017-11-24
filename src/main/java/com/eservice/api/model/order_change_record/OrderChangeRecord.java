package com.eservice.api.model.order_change_record;

import java.util.Date;
import javax.persistence.*;

@Table(name = "order_change_record")
public class OrderChangeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单编号
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 修改订单操作的用户ID，只有创建订单的销售员可以修改订单，或者销售经理
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 修改订单的时间
     */
    @Column(name = "change_time")
    private Date changeTime;

    /**
     * 更改原因
     */
    @Column(name = "change_reason")
    private String changeReason;

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
     * 获取订单编号
     *
     * @return order_id - 订单编号
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置订单编号
     *
     * @param orderId 订单编号
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取修改订单操作的用户ID，只有创建订单的销售员可以修改订单，或者销售经理
     *
     * @return user_id - 修改订单操作的用户ID，只有创建订单的销售员可以修改订单，或者销售经理
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置修改订单操作的用户ID，只有创建订单的销售员可以修改订单，或者销售经理
     *
     * @param userId 修改订单操作的用户ID，只有创建订单的销售员可以修改订单，或者销售经理
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取修改订单的时间
     *
     * @return change_time - 修改订单的时间
     */
    public Date getChangeTime() {
        return changeTime;
    }

    /**
     * 设置修改订单的时间
     *
     * @param changeTime 修改订单的时间
     */
    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    /**
     * 获取更改原因
     *
     * @return change_reason - 更改原因
     */
    public String getChangeReason() {
        return changeReason;
    }

    /**
     * 设置更改原因
     *
     * @param changeReason 更改原因
     */
    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
}