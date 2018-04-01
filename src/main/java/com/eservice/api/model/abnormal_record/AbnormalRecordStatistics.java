package com.eservice.api.model.abnormal_record;

import javax.persistence.*;
import java.util.Date;

@Table(name = "abnormal_record")
public class AbnormalRecordStatistics {

    /**
     * 获取异常类型
     *
     * @return abnormal_type - 异常类型
     */
    public Integer getAbnormalType() {
        return abnormalType;
    }

    /**
     * 设置异常类型
     *
     * @param abnormalType 异常类型
     */
    public void setAbnormalType(Integer abnormalType) {
        this.abnormalType = abnormalType;
    }


    public Integer getDateYear() {
        return dateYear;
    }

    public void setDateYear(Integer dateYear) {
        this.dateYear = dateYear;
    }

    public Integer getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(Integer dateMonth) {
        this.dateMonth = dateMonth;
    }

    public Integer getDateDay() {
        return dateDay;
    }

    public void setDateDay(Integer dateDay) {
        this.dateDay = dateDay;
    }

    public Integer getAbnormalCount() {
        return abnormalCount;
    }

    public void setAbnormalCount(Integer abnormalCount) {
        this.abnormalCount = abnormalCount;
    }

    public String getAbnormalName() {
        return abnormalName;
    }

    public void setAbnormalName(String abnormalName) {
        this.abnormalName = abnormalName;
    }

    private Integer abnormalType;
    private Integer abnormalCount;
    private String abnormalName;
    private Integer dateYear;
    private Integer dateMonth;
    private Integer dateDay;

}