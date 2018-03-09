package com.eservice.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eservice.api.dao.TaskPlanMapper;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.service.TaskPlanService;
import com.eservice.api.core.AbstractService;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.common.LinkDataModel;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/14.
*/
@Service
@Transactional
public class TaskPlanServiceImpl extends AbstractService<TaskPlan> implements TaskPlanService {
    @Resource
    private TaskPlanMapper taskPlanMapper;
    @Resource
    private MachineServiceImpl machineService;
    @Resource
    private TaskRecordServiceImpl taskRecordService;
    @Resource
    private ProcessRecordServiceImpl processRecordService;
    @Resource
    private CommonService commonService;

    public boolean addTaskPlans(@RequestParam List<Integer> taskRecordIds, Integer planType, String machineStrId, Date planDate, Integer userId) {
        for (int i = 0; i < taskRecordIds.size(); i++) {
            Condition tempCondition = new Condition(TaskPlan.class);
            tempCondition.createCriteria().andCondition("task_record_id = ", taskRecordIds.get(i));
            List<TaskPlan> existPlans = findByCondition(tempCondition);
            if(existPlans.size() > 0) {
                return false;
            }

            TaskPlan plan = new TaskPlan();
            plan.setCreateTime(new Date());
            plan.setUserId(userId);
            plan.setTaskRecordId(taskRecordIds.get(i));
            plan.setPlanType(Constant.DAILY_PLAN);

            if(planType.intValue() == Constant.DAILY_PLAN.intValue()) {
                plan.setPlanTime(planDate);
            }else if(planType.intValue() == Constant.FLEX_PLAN.intValue()) {
                plan.setDeadline(planDate);
            }
            save(plan);
            //更改task record状态为已计划
            TaskRecord taskRecord = taskRecordService.findById(taskRecordIds.get(i));
            if(taskRecord != null) {
                //检查是否为第一个计划项，如果是，需要设置为待安装状态
                Integer processRecordId =  taskRecord.getProcessRecordId();
                ProcessRecord  processRecord = processRecordService.findById(processRecordId);
                List<LinkDataModel> linkDataList = JSON.parseArray(processRecord.getLinkData(), LinkDataModel.class);
                for (LinkDataModel item: linkDataList) {
                    if(item.getTo().equals(taskRecord.getNodeKey().intValue())) {
                        if(item.getFrom() == null || item.getFrom() == -1) {
                            taskRecord.setStatus(Constant.TASK_INSTALL_WAITING);
///                            Integer machineId = processRecord.getMachineId();
//                            Machine machine = machineService.findById(machineId);
//                            //如果机器还处于"MACHINE_INSTALLING"之前，修改为安装中
//                            if(machine.getStatus() < Constant.MACHINE_INSTALLING) {
//                                machine.setStatus(Constant.MACHINE_INSTALLING);
//                                machine.setUpdateTime(new Date());
//                                machineService.update(machine);
///                            }
                            break;
                        }
                    }
                }
                if(taskRecord.getStatus().equals(Constant.TASK_INITIAL)) {
                    taskRecord.setStatus(Constant.TASK_PLANED);
                }
                taskRecordService.update(taskRecord);
                //更新task_record以外，但是跟task record相关的状态,机器状态，process_record中的task_status
                commonService.updateTaskRecordRelatedStatus(taskRecord);

            }else {
                //进行事务操作
                throw new RuntimeException();
            }
        }
        if(taskRecordIds.size() > 0 && machineStrId != null) {
            List<Machine> machineList = machineService.selectMachines(null, null, machineStrId, null, null, null, null, null, null, false);
            if(machineList.size() == 1) {
                //如果机器状态小于计划中，则更新为计划中
                Machine machine = machineList.get(0);
                if(machine.getStatus() < Constant.MACHINE_PLANING) {
                    machine.setStatus(Constant.MACHINE_PLANING);
                    machineService.update(machine);
                }
            }else {
                //进行事务rollback操作
                throw new RuntimeException();
            }
        }
        return true;
    }

    public List<TaskPlan> selectByUserAccount(String userAccount){
        return taskPlanMapper.selectByUserAccount(userAccount);
    }

}
