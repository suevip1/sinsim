package com.eservice.api.service.impl;

import com.eservice.api.dao.MachineMapper;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine.MachinePlan;
import com.eservice.api.model.machine.MachineInfo;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.service.MachineService;
import com.eservice.api.core.AbstractService;
import com.eservice.api.service.common.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/12/26.
 */
@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Machine> implements MachineService {
    @Resource
    private MachineMapper machineMapper;

    @Resource
    private TaskRecordServiceImpl taskRecordService;

    public Machine searchMachineByAbnormalRecordId(Integer abnormalRecordId) {
        return machineMapper.searchMachineByAbnormalRecordId(abnormalRecordId);
    }

    public Machine searchMachineByTaskQualityRecordId(Integer taskQualityRecordId) {
        return machineMapper.searchMachineByTaskQualityRecordId(taskQualityRecordId);
    }

    public List<Machine> selectMachines(Integer id,
                                        Integer order_id,
                                        String machine_strid,
                                        String nameplate,
                                        String location,
                                        Byte status,
                                        Integer machine_type,
                                        String query_start_time,
                                        String query_finish_time,
                                        Boolean is_fuzzy) {
        if (is_fuzzy) {
            return machineMapper.selectMachinesFuzzy(id, order_id, machine_strid, nameplate, location, status, machine_type, query_start_time, query_finish_time);
        } else {
            return machineMapper.selectMachines(id, order_id, machine_strid, nameplate, location, status, machine_type, query_start_time, query_finish_time);
        }
    }

    public List<MachinePlan> selectPlanningMachines(String orderNum,
                                                    String machine_strid,
                                                    String nameplate,
                                                    String location,
                                                    Byte status,
                                                    Integer machineType,
                                                    Integer dateType,
                                                    String query_start_time,
                                                    String query_finish_time,
                                                    Boolean is_fuzzy) {
        List<MachinePlan> machinePlanList = new ArrayList<>();
        List<MachinePlan> tempList = new ArrayList<>();
        if (is_fuzzy) {
            tempList = machineMapper.selectPlanningMachinesFuzzy(orderNum, machine_strid, nameplate, location, status, machineType, dateType, query_start_time, query_finish_time);
        } else {
            tempList = machineMapper.selectPlanningMachines(orderNum, machine_strid, nameplate, location, status, machineType, dateType, query_start_time, query_finish_time);
        }
        for (MachinePlan itemPlan : tempList) {
            if (itemPlan.getPlanedTaskNum() < itemPlan.getTotalTaskNum()) {
                machinePlanList.add(itemPlan);
            }
        }
        for (MachinePlan itemPlan : machinePlanList) {

            //获取机器对应task record,
            Condition tempCondition = new Condition(TaskRecord.class);
            tempCondition.createCriteria().andCondition("process_record_id = ", itemPlan.getProcessRecordID());
            tempCondition.createCriteria().andGreaterThanOrEqualTo("status", Constant.TASK_INITIAL);
            List<TaskRecord> taskRecordList = taskRecordService.findByCondition(tempCondition);
            HashMap<Byte, Integer> taskStatusMap = new HashMap<>();
            for (TaskRecord record : taskRecordList) {
                if (record.getStatus().equals(Constant.TASK_INSTALL_WAITING)) {
                    if (taskStatusMap.get(Constant.TASK_INSTALL_WAITING) != null) {
                        taskStatusMap.put(Constant.TASK_INSTALL_WAITING, taskStatusMap.get(Constant.TASK_INSTALL_WAITING) + 1);
                    } else {
                        taskStatusMap.put(Constant.TASK_INSTALL_WAITING, 1);
                    }
                } else if (record.getStatus().equals(Constant.TASK_INSTALLING)) {
                    if (taskStatusMap.get(Constant.TASK_INSTALLING) != null) {
                        taskStatusMap.put(Constant.TASK_INSTALLING, taskStatusMap.get(Constant.TASK_INSTALLING) + 1);
                    } else {
                        taskStatusMap.put(Constant.TASK_INSTALLING, 1);
                    }
                } else if (record.getStatus().equals(Constant.TASK_INSTALLED)) {
                    if (taskStatusMap.get(Constant.TASK_INSTALLED) != null) {
                        taskStatusMap.put(Constant.TASK_INSTALLED, taskStatusMap.get(Constant.TASK_INSTALLED) + 1);
                    } else {
                        taskStatusMap.put(Constant.TASK_INSTALLED, 1);
                    }
                } else if (record.getStatus().equals(Constant.TASK_QUALITY_DOING)) {
                    if (taskStatusMap.get(Constant.TASK_QUALITY_DOING) != null) {
                        taskStatusMap.put(Constant.TASK_QUALITY_DOING, taskStatusMap.get(Constant.TASK_QUALITY_DOING) + 1);
                    } else {
                        taskStatusMap.put(Constant.TASK_QUALITY_DOING, 1);
                    }
                } else if (record.getStatus().equals(Constant.TASK_QUALITY_DONE)) {
                    if (taskStatusMap.get(Constant.TASK_QUALITY_DONE) != null) {
                        taskStatusMap.put(Constant.TASK_QUALITY_DONE, taskStatusMap.get(Constant.TASK_QUALITY_DONE) + 1);
                    } else {
                        taskStatusMap.put(Constant.TASK_QUALITY_DONE, 1);
                    }
                } else if (record.getStatus().equals(Constant.TASK_INSTALL_ABNORMAL)) {
                    if (taskStatusMap.get(Constant.TASK_INSTALL_ABNORMAL) != null) {
                        taskStatusMap.put(Constant.TASK_INSTALL_ABNORMAL, taskStatusMap.get(Constant.TASK_INSTALL_ABNORMAL) + 1);
                    } else {
                        taskStatusMap.put(Constant.TASK_INSTALL_ABNORMAL, 1);
                    }
                } else if (record.getStatus().equals(Constant.TASK_QUALITY_ABNORMAL)) {
                    if (taskStatusMap.get(Constant.TASK_QUALITY_ABNORMAL) != null) {
                        taskStatusMap.put(Constant.TASK_QUALITY_ABNORMAL, taskStatusMap.get(Constant.TASK_QUALITY_ABNORMAL) + 1);
                    } else {
                        taskStatusMap.put(Constant.TASK_QUALITY_ABNORMAL, 1);
                    }
                }
            }
            itemPlan.setInstallWaitingTaskNum(taskStatusMap.get(Constant.TASK_INSTALL_WAITING));
            itemPlan.setInstalledTaskNum(taskStatusMap.get(Constant.TASK_INSTALLED));
            itemPlan.setInstallingTaskNum(taskStatusMap.get(Constant.TASK_INSTALLING));
            itemPlan.setInstallAbnormalTaskNum(taskStatusMap.get(Constant.TASK_INSTALL_ABNORMAL));
            itemPlan.setQualityDoingTaskNum(taskStatusMap.get(Constant.TASK_QUALITY_DOING));
            itemPlan.setQualityDoneTaskNum(taskStatusMap.get(Constant.TASK_QUALITY_DONE));
            itemPlan.setQualityAbnormalTaskNum(taskStatusMap.get(Constant.TASK_QUALITY_ABNORMAL));
        }

        return machinePlanList;
    }

    //selectConfigMachine
    public List<MachineInfo> selectConfigMachine(
            Integer order_id,
            String orderNum,
            String contractNum,
            String machine_strid,
            String nameplate,
            String location,
            Byte status,
            String query_start_time,
            String query_finish_time,
            Integer configStatus,
            Boolean is_fuzzy
    ) {
        if (is_fuzzy) {
            return machineMapper.selectConfigMachineFuzzy(order_id, orderNum, contractNum, machine_strid, nameplate, location, status, query_start_time, query_finish_time, configStatus);
        } else {
            return machineMapper.selectConfigMachine(order_id, orderNum, contractNum, machine_strid, nameplate, location, status, query_start_time, query_finish_time, configStatus);
        }
    }

    //selectProcessMachine
    public List<MachineInfo> selectProcessMachine(
            Integer order_id,
            String orderNum,
            String contractNum,
            String machine_strid,
            String nameplate,
            String location,
            Byte status,
            String query_start_time,
            String query_finish_time,
            Boolean is_fuzzy
    ) {
        if (is_fuzzy) {
            return machineMapper.selectProcessMachineFuzzy(order_id, orderNum, contractNum, machine_strid, nameplate, location, status, query_start_time, query_finish_time);
        } else {
            return machineMapper.selectProcessMachine(order_id, orderNum, contractNum, machine_strid, nameplate, location, status, query_start_time, query_finish_time);
        }
    }

    public Machine selectMachinesByNameplate(String nameplate) {
        return machineMapper.selectMachinesByNameplate(nameplate);
    }
}
