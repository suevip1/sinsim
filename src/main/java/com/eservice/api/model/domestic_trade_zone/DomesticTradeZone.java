package com.eservice.api.model.domestic_trade_zone;

import javax.persistence.*;

@Table(name = "domestic_trade_zone")
public class DomesticTradeZone {
    /**
     * 内贸部门分区.
     * 内贸也分区域，但是销售员和区域则不绑定。
     * 选了内贸区域以后，则要固定的内贸销售经理来签核。其他区域的销售经理不可见。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 分区名字
     */
    @Column(name = "zone_name")
    private String zoneName;

    /**
     * 负责人
     */
    @Column(name = "owner_id")
    private Integer ownerId;

    /**
     * 获取内贸部门分区
     *
     * @return id - 内贸部门分区
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置内贸部门分区
     *
     * @param id 内贸部门分区
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取分区名字
     *
     * @return zone_name - 分区名字
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * 设置分区名字
     *
     * @param zoneName 分区名字
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    /**
     * 获取负责人
     *
     * @return owner_id - 负责人
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * 设置负责人
     *
     * @param ownerId 负责人
     */
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}