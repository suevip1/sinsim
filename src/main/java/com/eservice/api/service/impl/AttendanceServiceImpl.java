package com.eservice.api.service.impl;

import com.eservice.api.dao.AttendanceMapper;
import com.eservice.api.model.attendance.Attendance;
import com.eservice.api.service.AttendanceService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


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

}
