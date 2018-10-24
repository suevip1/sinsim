package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.common.NodeDataModel;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.eservice.api.service.impl.ProcessRecordServiceImpl;
import com.eservice.api.service.impl.TaskPlanServiceImpl;
import com.eservice.api.service.impl.TaskRecordServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/11/14.
 */
@RestController
@RequestMapping("/process/record")
public class ProcessRecordController {
    @Resource
    private ProcessRecordServiceImpl processRecordService;
    @Resource
    private TaskRecordServiceImpl taskRecordService;
    @Resource
    private MachineServiceImpl machineService;
    @Resource
    private TaskPlanServiceImpl taskPlanService;

    private Logger logger = Logger.getLogger(ProcessRecordController.class);

    @PostMapping("/add")
    public Result add(ProcessRecord processRecord) {
        processRecordService.save(processRecord);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        processRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String processRecord) {
        ProcessRecord processRecord1 = JSON.parseObject(processRecord, ProcessRecord.class);
        processRecordService.update(processRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ProcessRecord processRecord = processRecordService.findById(id);
        return ResultGenerator.genSuccessResult(processRecord);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ProcessRecord> list = processRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    @PostMapping("/addProcessForMachine")
    @Transactional(rollbackFor = Exception.class)
    public Result addProcessForMachine(String taskRecords, String processRecord, String machine) {
        ProcessRecord pr = JSON.parseObject(processRecord, ProcessRecord.class);
        List<TaskRecord> trList = JSON.parseArray(taskRecords, TaskRecord.class);
        Machine machineObj = JSON.parseObject(machine, Machine.class);
        if (pr == null || trList == null) {
            return ResultGenerator.genFailResult("提交到服务端的JSON数据解析错误");
        }

        Integer prId = pr.getId();
        pr.setCreateTime(new Date());
        boolean firstCreate = true;
        try {
            //已经保存过配置流程的，需要更新process
            if (prId != null && prId > 0) {
                firstCreate = false;
                processRecordService.update(pr);
            } else {
                processRecordService.save(pr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultGenerator.genFailResult("processRecordService数据库操作失败");
        }

        try {
            if (firstCreate) {
                //trList只有在第一次创建时才会传递值，更新是为[]
                trList.forEach((item) -> {
                    item.setProcessRecordId(pr.getId());
                });
                taskRecordService.save(trList);
            } else {
                //更新时数据在process中
                List<NodeDataModel> nodeDataModelList = JSON.parseArray(pr.getNodeData(), NodeDataModel.class);
                //找到之前属于该process ID的所有task， 对比传递进入的 task records
                Condition tempCondition = new Condition(TaskRecord.class);
                tempCondition.createCriteria().andCondition("process_record_id = ", prId);
                List<TaskRecord> existTaskRecords = taskRecordService.findByCondition(tempCondition);
                //删除不存在的list
                List<TaskRecord> validTaskRecordList = new ArrayList<>();
                for (TaskRecord exist : existTaskRecords) {
                    boolean same = false;
                    for (int i = 0; i < nodeDataModelList.size() && !same; i++) {
                        if (nodeDataModelList.get(i).getKey().equals("-1") || nodeDataModelList.get(i).getKey().equals("-4")) {

                        } else {
                            //task name和key值必须一致，否则会导致不统一
                            if (nodeDataModelList.get(i).getText().equals(exist.getTaskName()) && nodeDataModelList.get(i).getKey().equals(String.valueOf(exist.getNodeKey()))) {
                                same = true;
                            }
                        }
                    }
                    if (!same) {
                        //删除该节点，但是已经安装后的工序不能删除，业务需要
                        if (exist.getStatus().intValue() < Constant.TASK_INSTALLING.intValue()) {
                            if (exist.getStatus().intValue() != Constant.TASK_INITIAL) {
                                //删除task plan中对应数据，否则外键关联会导致失败
                                Condition taskPlanCondition = new Condition(TaskPlan.class);
                                taskPlanCondition.createCriteria().andCondition("task_record_id = ", exist.getId());
                                List<TaskPlan> taskPlanList = taskPlanService.findByCondition(taskPlanCondition);
                                //只能是一个
                                if(taskPlanList.size() == 1) {
                                    taskPlanService.deleteById(taskPlanList.get(0).getId());
                                }
                            }
                            taskRecordService.deleteById(exist.getId());
                            logger.warn("删除Task Record: ==> Name: " + exist.getTaskName() + ", ID: " + exist.getId() + "Status: " + exist.getStatus() + "Process Record ID: " + pr.getId());
                        }
                    } else {
                        validTaskRecordList.add(exist);
                    }
                }
                //添加新的task record
                List<TaskRecord> newAddedList = new ArrayList<>();
                for (NodeDataModel item : nodeDataModelList) {
                    if(Integer.valueOf(item.getKey()).intValue() == -1 || Integer.valueOf(item.getKey()).intValue() == -4) {
                        continue;
                    }
                    boolean newAdded = true;
                    for (int i = 0; i < validTaskRecordList.size() && newAdded; i++) {
                        if(Integer.valueOf(item.getKey()).intValue() == validTaskRecordList.get(i).getNodeKey().intValue()) {
                            newAdded = false;
                        }
                    }
                    if(newAdded) {
                        TaskRecord record = new TaskRecord();
                        record.setStatus(Byte.valueOf("0"));
                        record.setProcessRecordId(pr.getId());
                        record.setNodeKey(Byte.valueOf(item.getKey()));
                        record.setTaskName(item.getText());
                        newAddedList.add(record);
                    }
                }
                if (newAddedList.size() > 0) {
                    taskRecordService.save(newAddedList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultGenerator.genFailResult("taskRecordService数据库操作失败");
        }

        try {
            if (machineObj.getId() == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultGenerator.genFailResult("机器Id为空，数据更新失败");
            }
            //如果机器处于初始化状态，则设置为“已配置”,如果已经是其他状态，则不需要更改机器状态
            if (machineObj.getStatus().equals(Constant.MACHINE_INITIAL)) {
                machineObj.setStatus(Constant.MACHINE_CONFIGURED);
            }
            machineService.update(machineObj);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultGenerator.genFailResult("machineService数据库操作失败");
        }
        return ResultGenerator.genSuccessResult();

    }
}
