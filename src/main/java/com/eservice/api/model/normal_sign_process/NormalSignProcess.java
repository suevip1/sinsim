package com.eservice.api.model.normal_sign_process;

import java.util.Date;
import javax.persistence.*;

@Table(name = "normal_sign_process")
public class NormalSignProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 流程序列编号
     */
    private Byte number;

    /**
     * 签核流程的角色
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 流程环节属于合同还是需求单
     */
    private Byte type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
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
     * 获取流程序列编号
     *
     * @return number - 流程序列编号
     */
    public Byte getNumber() {
        return number;
    }

    /**
     * 设置流程序列编号
     *
     * @param number 流程序列编号
     */
    public void setNumber(Byte number) {
        this.number = number;
    }

    /**
     * 获取签核流程的角色
     *
     * @return role_id - 签核流程的角色
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置签核流程的角色
     *
     * @param roleId 签核流程的角色
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取流程环节属于合同还是需求单
     *
     * @return type - 流程环节属于合同还是需求单
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置流程环节属于合同还是需求单
     *
     * @param type 流程环节属于合同还是需求单
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
}