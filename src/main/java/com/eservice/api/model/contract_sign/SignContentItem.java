package com.eservice.api.model.contract_sign;

import java.util.Date;

/**
 * Class Description:
 *
 * @author Wilson Hu
 * @date 21/12/2017
 */
public class SignContentItem {
    /**
     * 签核的顺位
     */
    private Integer number;

    /**
     * 签核的角色ID
     */
    private Integer roleId;

    /**
     * 签核的人
     */
    private String user;

    /**

     * 签核意见
     */
    private String comment;

    /**
     * 签核的类型（合同签核/需求单签核）
     */
    private String signType;

    /**
     * 签核结果
     * "0" --> "初始化"
     * "1" --> "同意"
     * "2" --> "拒绝"
     */
    private Integer result;

    /**
     * 签核时间
     */
    private Date date;

    /**
     * true表示该部门需要审核, 联系单需要在创建时指定审核部门。
     */
    private Boolean isEnabled;

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public String getSignType() {
        return signType;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getDate(){ return this.date; }

    public void setDate(Date date) {this.date = date; }
}
