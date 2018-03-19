package com.eservice.api.model.abnormal;

import java.util.Date;
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
     * 异常是否有效，前端删除某个异常类型时，如果该类型被使用过，valid设置为0，未使用过则删除，默认为1
     */
    private Integer valid;

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

    /**
     * 获取异常是否有效，前端删除某个异常类型时，如果该类型被使用过，valid设置为0，未使用过则删除，默认为1
     *
     * @return valid - 异常是否有效，前端删除某个异常类型时，如果该类型被使用过，valid设置为0，未使用过则删除，默认为1
     */
    public Integer getValid() {
        return valid;
    }

    /**
     * 设置异常是否有效，前端删除某个异常类型时，如果该类型被使用过，valid设置为0，未使用过则删除，默认为1
     *
     * @param valid 异常是否有效，前端删除某个异常类型时，如果该类型被使用过，valid设置为0，未使用过则删除，默认为1
     */
    public void setValid(Integer valid) {
        this.valid = valid;
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