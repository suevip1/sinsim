package com.eservice.api.web.wrapper;

import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.dashboard.Statistic;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordExpired;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.eservice.api.service.impl.TaskRecordServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.crypto.Mac;
import java.util.List;

/**
 * Class Description: 对应前端dashboard的API实现
 *
 * @author Wilson Hu
 * @date 2018/03/28.
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Resource
    private MachineServiceImpl machineService;
    @Resource
    private TaskRecordServiceImpl taskRecordService;

    /**
     * @return 返回dashboard中用到的安装中、改拆单等数据信息
     */
    @PostMapping("/getStatistic")
    public Result getStatistics() {

        Statistic statistic = new Statistic();
        Condition condition = new Condition(Machine.class);

        condition.createCriteria()
                .andCondition("status !=", Constant.MACHINE_INITIAL)
                .andCondition("status !=", Constant.MACHINE_CONFIGURED)
                .andCondition("status !=", Constant.MACHINE_CANCELED)
                .andCondition("status !=", Constant.MACHINE_INSTALLED);
        List<Machine> machineList = machineService.findByCondition(condition);
        int installingNum = machineList.size();
        int changeNum = 0;
        int splitNum = 0;
        for (Machine item : machineList) {
            if (item.getStatus().equals(Constant.MACHINE_CHANGED)) {
                changeNum++;
            } else if (item.getStatus().equals(Constant.MACHINE_SPLITED)) {
                splitNum++;
            }
        }
        statistic.setInstallingMachineNum(installingNum);
        statistic.setChangeMachineNum(changeNum);
        statistic.setSplitMachineNum(splitNum);

        //获取异常工序数量
        Condition taskRecordCondition = new Condition(TaskRecord.class);
        taskRecordCondition.createCriteria()
                .andCondition("status >= ", Constant.TASK_INSTALL_ABNORMAL)
                .andCondition("status <= ", Constant.TASK_QUALITY_ABNORMAL);
        List<TaskRecord> abnormalTaskRecordList = taskRecordService.findByCondition(taskRecordCondition);
        statistic.setAbnormalTaskNum(abnormalTaskRecordList.size());
        return ResultGenerator.genSuccessResult(statistic);
    }

    @PostMapping("/getExpiredTaskStatistics")
    public Result getExpiredTaskStatistics(Integer mode) {
        List<TaskRecordExpired> list = taskRecordService.getExpiredTaskStatistics(mode);
        return ResultGenerator.genSuccessResult(list);
    }
}
