package com.eservice.api.model.order_cancel_record;

import java.util.Date;
import javax.persistence.*;

@Table(name = "order_cancel_record")
public class OrderCancelRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单编号
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 取消用户的ID，只有创建订单的销售员可以取消改订单，或者销售经理
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 取消时间
     */
    @Column(name = "cancel_time")
    private Date cancelTime;

    /**
     * 取消原因
     */
    @Column(name = "cancel_reason")
    private String cancelReason;

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
     * 获取取消用户的ID，只有创建订单的销售员可以取消改订单，或者销售经理
     *
     * @return user_id - 取消用户的ID，只有创建订单的销售员可以取消改订单，或者销售经理
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置取消用户的ID，只有创建订单的销售员可以取消改订单，或者销售经理
     *
     * @param userId 取消用户的ID，只有创建订单的销售员可以取消改订单，或者销售经理
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取取消时间
     *
     * @return cancel_time - 取消时间
     */
    public Date getCancelTime() {
        return cancelTime;
    }

    /**
     * 设置取消时间
     *
     * @param cancelTime 取消时间
     */
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    /**
     * 获取取消原因
     *
     * @return cancel_reason - 取消原因
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * 设置取消原因
     *
     * @param cancelReason 取消原因
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}