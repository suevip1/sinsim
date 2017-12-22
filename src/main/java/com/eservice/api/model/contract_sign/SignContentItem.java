package com.eservice.api.model.contract_sign;

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
}
