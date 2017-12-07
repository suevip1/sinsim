package com.eservice.api.model.change_sign_process;

import javax.persistence.*;

@Table(name = "change_sign_process")
public class ChangeSignProcess {
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
}