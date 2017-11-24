package com.eservice.api.model.device;

import javax.persistence.*;

public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * MEID地址
     */
    private String meid;

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
     * 获取设备名称
     *
     * @return name - 设备名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置设备名称
     *
     * @param name 设备名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取mac地址
     *
     * @return meid - MEID地址
     */
    public String getMeid() {
        return meid;
    }

    /**
     * 设置mac地址
     *
     * @param meid MEID地址
     */
    public void setMeid(String meid) {
        this.meid = meid;
    }
}