package com.eservice.api.model.attendance;

import com.eservice.api.model.user.User;

public class AttendanceDetail extends Attendance{

    private String userAccount;

    private String installGroupName;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getInstallGroupName() {
        return installGroupName;
    }

    public void setInstallGroupName(String installGroupName) {
        this.installGroupName = installGroupName;
    }
}
