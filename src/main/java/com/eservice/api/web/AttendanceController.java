package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.attendance.Attendance;
import com.eservice.api.model.attendance.AttendanceDetail;
import com.eservice.api.service.AttendanceService;
import com.eservice.api.service.UserService;
import com.eservice.api.service.impl.AttendanceServiceImpl;
import com.eservice.api.service.impl.InstallGroupServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/07/25.
*/
@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Resource
    private AttendanceServiceImpl attendanceService;

    @Resource
    private UserService userService;

    @Resource
    private InstallGroupServiceImpl installGroupService;

    @PostMapping("/add")
    public Result add(String attendance) {
        Attendance attendance1 = JSON.parseObject(attendance, Attendance.class);
        if (attendance1 != null) {
            if (attendance1.getUserId() == null) {
                return ResultGenerator.genFailResult("错误，uerId 为 null！");
            } else if(userService.findById(attendance1.getUserId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该uerId " + attendance1.getUserId() + " 找不到对应的 user！");
            }

            if (attendance1.getInstallGroupId() == null) {
                return ResultGenerator.genFailResult("错误，getInstallGroupId 为 null！");
            } else if (installGroupService.findById(attendance1.getInstallGroupId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该InstallGroupId " + attendance1.getInstallGroupId() + " 找不到对应的 installGroup！");
            }
            //todo user和installGroup要匹配
            attendance1.setDate(new Date());
            attendanceService.save(attendance1);
        } else {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        attendanceService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String attendance) {
        Attendance attendance1 = JSON.parseObject(attendance, Attendance.class);
        if (attendance1 != null) {
            attendanceService.update(attendance1);
        } else {
            return ResultGenerator.genFailResult("参数不正确，更新失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Attendance attendance = attendanceService.findById(id);
        return ResultGenerator.genSuccessResult(attendance);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Attendance> list = attendanceService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据条件查询出勤信息
     * @param page
     * @param size
     * @param userAccount
     * @param installGroupName
     * @param queryStartTime
     * @param queryFinishTime
     * @return
     */
    @PostMapping("/selectAttendanceDetails")
    public Result selectAttendanceDetails(@RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "0") Integer size,
                                          String userAccount,
                                          String installGroupName,
                                          String queryStartTime,
                                          String queryFinishTime) {
        PageHelper.startPage(page, size);
        List<AttendanceDetail> list = attendanceService.selectAttendanceDetails(userAccount,installGroupName,queryStartTime,queryFinishTime);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
