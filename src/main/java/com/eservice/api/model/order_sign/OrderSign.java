package com.eservice.api.model.order_sign;

import java.util.Date;
import javax.persistence.*;

@Table(name = "order_sign")
public class OrderSign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 签核状态：“1”==>签核中， “2”==>签核完成， “3”==>驳回，该条记录在驳回后停止修改，会新创建签核记录
     */
    private Byte status;

    /**
     * 签核流程开始时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成
[ 
    {"role_id": 1, "role_name":"销售经理"，“person”：“张三”，”comment“: "同意"， ”update_time“:"2017-11-05 12:08:55"},
    {"role_id":2, "role_name":"技术部"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"， ”update_time“:"2017-11-06 12:08:55"}
]
     */
    @Column(name = "sign_content")
    private String signContent;

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
     * 获取订单ID
     *
     * @return order_id - 订单ID
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置订单ID
     *
     * @param orderId 订单ID
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取签核状态：“1”==>签核中， “2”==>签核完成， “3”==>驳回，该条记录在驳回后停止修改，会新创建签核记录
     *
     * @return status - 签核状态：“1”==>签核中， “2”==>签核完成， “3”==>驳回，该条记录在驳回后停止修改，会新创建签核记录
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置签核状态：“1”==>签核中， “2”==>签核完成， “3”==>驳回，该条记录在驳回后停止修改，会新创建签核记录
     *
     * @param status 签核状态：“1”==>签核中， “2”==>签核完成， “3”==>驳回，该条记录在驳回后停止修改，会新创建签核记录
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取签核流程开始时间
     *
     * @return create_time - 签核流程开始时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置签核流程开始时间
     *
     * @param createTime 签核流程开始时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成
[ 
    {"role_id": 1, "role_name":"销售经理"，“person”：“张三”，”comment“: "同意"， ”update_time“:"2017-11-05 12:08:55"},
    {"role_id":2, "role_name":"技术部"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"， ”update_time“:"2017-11-06 12:08:55"}
]
     *
     * @return sign_content - 签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成
[ 
    {"role_id": 1, "role_name":"销售经理"，“person”：“张三”，”comment“: "同意"， ”update_time“:"2017-11-05 12:08:55"},
    {"role_id":2, "role_name":"技术部"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"， ”update_time“:"2017-11-06 12:08:55"}
]
     */
    public String getSignContent() {
        return signContent;
    }

    /**
     * 设置签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成
[ 
    {"role_id": 1, "role_name":"销售经理"，“person”：“张三”，”comment“: "同意"， ”update_time“:"2017-11-05 12:08:55"},
    {"role_id":2, "role_name":"技术部"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"， ”update_time“:"2017-11-06 12:08:55"}
]
     *
     * @param signContent 签核内容，以json格式的数组形式存放, 所有项完成后更新status为完成
[ 
    {"role_id": 1, "role_name":"销售经理"，“person”：“张三”，”comment“: "同意"， ”update_time“:"2017-11-05 12:08:55"},
    {"role_id":2, "role_name":"技术部"，“person”：“李四”，”comment“: "同意，但是部分配件需要新设计"， ”update_time“:"2017-11-06 12:08:55"}
]
     */
    public void setSignContent(String signContent) {
        this.signContent = signContent;
    }
}