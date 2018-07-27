package com.eservice.api.model.order_split_record;

import java.util.Date;
import javax.persistence.*;

@Table(name = "order_split_record")
public class OrderSplitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 新订单编号
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 被拆需求单编号
     */
    @Column(name = "original_order_id")
    private Integer originalOrderId;

    /**
     * 拆分订单操作的用户ID，只有创建订单的销售员可以拆分改订单，或者销售经理
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 拆分订单的时间
     */
    @Column(name = "split_time")
    private Date splitTime;

    /**
     * 取消原因
     */
    @Column(name = "split_reason")
    private String splitReason;

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

    public Integer getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Integer originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    /**
     * 获取拆分订单操作的用户ID，只有创建订单的销售员可以拆分改订单，或者销售经理
     *
     * @return user_id - 拆分订单操作的用户ID，只有创建订单的销售员可以拆分改订单，或者销售经理
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置拆分订单操作的用户ID，只有创建订单的销售员可以拆分改订单，或者销售经理
     *
     * @param userId 拆分订单操作的用户ID，只有创建订单的销售员可以拆分改订单，或者销售经理
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取拆分订单的时间
     *
     * @return split_time - 拆分订单的时间
     */
    public Date getSplitTime() {
        return splitTime;
    }

    /**
     * 设置拆分订单的时间
     *
     * @param splitTime 拆分订单的时间
     */
    public void setSplitTime(Date splitTime) {
        this.splitTime = splitTime;
    }

    /**
     * 获取取消原因
     *
     * @return split_reason - 取消原因
     */
    public String getSplitReason() {
        return splitReason;
    }

    /**
     * 设置取消原因
     *
     * @param splitReason 取消原因
     */
    public void setSplitReason(String splitReason) {
        this.splitReason = splitReason;
    }
}