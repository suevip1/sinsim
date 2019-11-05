package com.eservice.api.model.change_item;

import javax.persistence.*;

@Table(name = "change_item")
public class ChangeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 对应联系单的ID，一个联系单可以有多个变更内容。
     */
    @Column(name = "contact_form_id")
    private Integer contactFormId;

    /**
     * 旧状态(变更前）
     */
    @Column(name = "old_info")
    private String oldInfo;

    /**
     * 新状态（变更后）
     */
    @Column(name = "new_info")
    private String newInfo;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 附件
     */
    @Column(name = "attached_file")
    private String attachedFile;

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
     * 获取对应联系单的ID，一个联系单可以有多个变更内容。
     *
     * @return contact_form_id - 对应联系单的ID，一个联系单可以有多个变更内容。
     */
    public Integer getContactFormId() {
        return contactFormId;
    }

    /**
     * 设置对应联系单的ID，一个联系单可以有多个变更内容。
     *
     * @param contactFormId 对应联系单的ID，一个联系单可以有多个变更内容。
     */
    public void setContactFormId(Integer contactFormId) {
        this.contactFormId = contactFormId;
    }

    /**
     * 获取旧状态(变更前）
     *
     * @return old_info - 旧状态(变更前）
     */
    public String getOldInfo() {
        return oldInfo;
    }

    /**
     * 设置旧状态(变更前）
     *
     * @param oldInfo 旧状态(变更前）
     */
    public void setOldInfo(String oldInfo) {
        this.oldInfo = oldInfo;
    }

    /**
     * 获取新状态（变更后）
     *
     * @return new_info - 新状态（变更后）
     */
    public String getNewInfo() {
        return newInfo;
    }

    /**
     * 设置新状态（变更后）
     *
     * @param newInfo 新状态（变更后）
     */
    public void setNewInfo(String newInfo) {
        this.newInfo = newInfo;
    }

    /**
     * 获取备注
     *
     * @return remarks - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取附件
     *
     * @return attached_file - 附件
     */
    public String getAttachedFile() {
        return attachedFile;
    }

    /**
     * 设置附件
     *
     * @param attachedFile 附件
     */
    public void setAttachedFile(String attachedFile) {
        this.attachedFile = attachedFile;
    }
}