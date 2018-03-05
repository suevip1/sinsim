package com.eservice.api.model.order_loading_list;

import java.util.Date;
import javax.persistence.*;

@Table(name = "order_loading_list")
public class OrderLoadingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 装车单、联系单对应的订单id，多张图片对应多个记录
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 装车单、联系单对应的Excel文件名（包含路径）,多个的话对应多条记录
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * "1"==>装车单，"2"==>联系单
     */
    private Byte type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取装车单、联系单对应的订单id，多张图片对应多个记录
     *
     * @return order_id - 装车单、联系单对应的订单id，多张图片对应多个记录
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置装车单、联系单对应的订单id，多张图片对应多个记录
     *
     * @param orderId 装车单、联系单对应的订单id，多张图片对应多个记录
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取装车单、联系单对应的Excel文件名（包含路径）,多个的话对应多条记录
     *
     * @return file_name - 装车单、联系单对应的Excel文件名（包含路径）,多个的话对应多条记录
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置装车单、联系单对应的Excel文件名（包含路径）,多个的话对应多条记录
     *
     * @param fileName 装车单、联系单对应的Excel文件名（包含路径）,多个的话对应多条记录
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取"1"==>装车单，"2"==>联系单
     *
     * @return type - "1"==>装车单，"2"==>联系单
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置"1"==>装车单，"2"==>联系单
     *
     * @param type "1"==>装车单，"2"==>联系单
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}