package com.eservice.api.model.role;

import javax.persistence.*;

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    public String getRoleScope() {
        return roleScope;
    }

    public void setRoleScope(String roleScope) {
        this.roleScope = roleScope;
    }

    /**
     * 角色对应的权限列表
     */
    @Column(name = "role_scope")
    private String roleScope;

    /**
     * 角色说明
     */
    @Column(name = "role_des")
    private String roleDes;

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
     * @return role_name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取角色说明
     *
     * @return role_des - 角色说明
     */
    public String getRoleDes() {
        return roleDes;
    }

    /**
     * 设置角色说明
     *
     * @param roleDes 角色说明
     */
    public void setRoleDes(String roleDes) {
        this.roleDes = roleDes;
    }
}