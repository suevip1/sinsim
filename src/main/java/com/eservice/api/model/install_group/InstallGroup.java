package com.eservice.api.model.install_group;

import javax.persistence.*;

@Table(name = "install_group")
public class InstallGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 公司部门
     */
    @Column(name = "group_name")
    private String groupName;

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
     * 获取公司部门
     *
     * @return group_name - 公司部门
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置公司部门
     *
     * @param groupName 公司部门
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}