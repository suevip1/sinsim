package com.eservice.api.model.user;

import com.eservice.api.model.install_group.InstallGroup;
import com.eservice.api.model.role.Role;

public class UserDetail {

    private Integer id;

    private Role role;

    private InstallGroup group;

    public InstallGroup getGroup() {
        return group;
    }

    public void setGroup(InstallGroup group) {
        this.group = group;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * 账号
     */
    private String account;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 姓名
     */
    private String name;

//    /**
//     * 角色ID
//     */
//    private Integer roleId;
//
//    /**
//     * 密码
//     */
//    private String password;

//    /**
//     * 所在安装组group ID，可以为空(其他部门人员)
//     */
//    private Integer groupId;

    /**
     * 员工是否在职，“1”==>在职, “0”==>离职
     */
    private Byte valid;

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
     * 获取账号
     *
     * @return account - 账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置账号
     *
     * @param account 账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取角色ID
     *
     * @return role_id - 角色ID
     */
//    public Integer getRoleId() {
//        return roleId;
//    }

    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
//    public void setRoleId(Integer roleId) {
//        this.roleId = roleId;
//    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
//    public String getPassword() {
//        return password;
//    }

//    /**
//     * 设置密码
//     *
//     * @param password 密码
//     */
//    public void setPassword(String password) {
//        this.password = password;
//    }

    /**
     * 获取所在安装组group ID，可以为空(其他部门人员)
     *
     * @return group_id - 所在安装组group ID，可以为空(其他部门人员)
     */
//    public Integer getGroupId() {
//        return groupId;
//    }

    /**
     * 设置所在安装组group ID，可以为空(其他部门人员)
     *
     * @param groupId 所在安装组group ID，可以为空(其他部门人员)
     */
//    public void setGroupId(Integer groupId) {
//        this.groupId = groupId;
//    }

    /**
     * 获取员工是否在职，“1”==>在职, “0”==>离职
     *
     * @return valid - 员工是否在职，“1”==>在职, “0”==>离职
     */
    public Byte getValid() {
        return valid;
    }

    /**
     * 设置员工是否在职，“1”==>在职, “0”==>离职
     *
     * @param valid 员工是否在职，“1”==>在职, “0”==>离职
     */
    public void setValid(Byte valid) {
        this.valid = valid;
    }
}