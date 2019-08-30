package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.attendance.Attendance;
import com.eservice.api.model.attendance.AttendanceDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttendanceMapper extends Mapper<Attendance> {
    List<AttendanceDetail> selectAttendanceDetails(@Param("userAccount") String userAccount,
                                                   @Param("installGroupName") String installGroupName,
                                                   @Param("queryStartTime") String queryStartTime,
                                                   @Param("queryFinishTime") String queryFinishTime);
}