package com.eservice.api.model.market_group;

import javax.persistence.*;

@Table(name = "market_group")
public class MarketGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 销售部各组名称
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
     * 获取销售部各组名称
     *
     * @return group_name - 销售部各组名称
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置销售部各组名称
     *
     * @param groupName 销售部各组名称
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}