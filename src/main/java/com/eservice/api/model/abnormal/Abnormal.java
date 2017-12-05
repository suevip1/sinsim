package com.eservice.api.model.abnormal;

import javax.persistence.*;

public class Abnormal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 异常名称
     */
    @Column(name = "abnormal_name")
    private String abnormalName;

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
     * 获取异常名称
     *
     * @return abnormal_name - 异常名称
     */
    public String getAbnormalName() {
        return abnormalName;
    }

    /**
     * 设置异常名称
     *
     * @param abnormalName 异常名称
     */
    public void setAbnormalName(String abnormalName) {
        this.abnormalName = abnormalName;
    }
}