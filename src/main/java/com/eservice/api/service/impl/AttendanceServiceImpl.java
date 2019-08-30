package com.eservice.api.service.impl;

import com.eservice.api.dao.AttendanceMapper;
import com.eservice.api.model.attendance.Attendance;
import com.eservice.api.model.attendance.AttendanceDetail;
import com.eservice.api.service.AttendanceService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/07/25.
*/
@Service
@Transactional
public class AttendanceServiceImpl extends AbstractService<Attendance> implements AttendanceService {
    @Resource
    private AttendanceMapper attendanceMapper;

    public List<AttendanceDetail> selectAttendanceDetails(String userAccount,
                                                          String installGroupName,
                                                          String queryStartTime,
                                                          String queryFinishTime){
        return attendanceMapper.selectAttendanceDetails(userAccount,installGroupName,queryStartTime,queryFinishTime);
    }

}
