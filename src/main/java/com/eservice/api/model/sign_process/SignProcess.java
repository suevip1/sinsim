package com.eservice.api.model.sign_process;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sign_process")
public class SignProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 签核流程的名称
     */
    @Column(name = "process_name")
    private String processName;

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
     * 签核流程内容，json格式，每一个step为序号和对应角色
     [
        {"step":1, "role_id":1}.
        {"step":2, "role_id":3}
     ]
     */
    @Column(name = "process_content")
    private String processContent;

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
     * 获取签核流程的名称
     *
     * @return process_name - 签核流程的名称
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * 设置签核流程的名称
     *
     * @param processName 签核流程的名称
     */
    public void setProcessName(String processName) {
        this.processName = processName;
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

    /**
     * 获取签核流程内容，json格式
     *
     * @return process_content - 签核流程内容，json格式
     */
    public String getProcessContent() {
        return processContent;
    }

    /**
     * 设置签核流程内容，json格式
     *
     * @param processContent 签核流程内容，json格式
     */
    public void setProcessContent(String processContent) {
        this.processContent = processContent;
    }
}