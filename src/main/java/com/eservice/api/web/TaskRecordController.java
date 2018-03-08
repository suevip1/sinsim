package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.order_loading_list.OrderLoadingList;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
import com.eservice.api.model.user.User;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.common.Utils;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.eservice.api.service.impl.ProcessRecordServiceImpl;
import com.eservice.api.service.impl.TaskRecordServiceImpl;
import com.eservice.api.service.impl.UserServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.eservice.api.service.common.NodeDataModel;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/12/01.
 */
@RestController
@RequestMapping("/task/record")
public class TaskRecordController {
    @Resource
    private TaskRecordServiceImpl taskRecordService;
    @Resource
    private ProcessRecordServiceImpl processRecordService;
    @Resource
    private MachineServiceImpl machineService;

    @PostMapping("/add")
    public Result add(String taskRecord) {
        TaskRecord taskRecord1 = JSON.parseObject(taskRecord, TaskRecord.class);
        taskRecordService.save(taskRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        taskRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String taskRecord) {
        TaskRecord taskRecord1 = JSON.parseObject(taskRecord, TaskRecord.class);
        taskRecordService.update(taskRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        TaskRecord taskRecord = taskRecordService.findById(id);
        return ResultGenerator.genSuccessResult(taskRecord);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据userAccount 返回该用户的 Tasks
     *
     * @param page
     * @param size
     * @param userAccount
     * @return 返回该用户的 Tasks
     */
    @PostMapping("/selectTaskReocords")
    public Result selectTaskReocords(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                     @RequestParam String userAccount) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.selectTaskReocords(userAccount);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据 taskRecord.id 返回 task_plans
     *
     * @param page
     * @param size
     * @param taskRecordId
     * @return
     */
    @PostMapping("/selectTaskPlans")
    public Result selectTaskPlans(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                  @RequestParam Integer taskRecordId) {
        PageHelper.startPage(page, size);
        List<TaskPlan> list = taskRecordService.selectTaskPlans(taskRecordId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据  taskRecord.id 返回processRecord, machine，machine order,order_loading_list 信息。
     *
     * @param taskRecordId
     * @return
     */
    @PostMapping("selectTaskRecordDetail")
    public Result selectTaskRecordDetail(@RequestParam Integer taskRecordId) {
        TaskRecordDetail taskRecordDetail = taskRecordService.selectTaskRecordDetail(taskRecordId);
        return ResultGenerator.genSuccessResult(taskRecordDetail);
    }

    /**
     * 给生产部管理员返回所有detail，其中不限于包括：
     * {
     * "machine_id":"",
     * "task_name":"",
     * "status":"",
     * "交货日期":"",
     * "计划日期":"",
     * }
     *
     * @param page
     * @param size
     * @return
     */
    @PostMapping("selectAllTaskRecordDetail")
    public Result selectAllTaskRecordDetail(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> ListTaskRecordDetail = taskRecordService.selectAllTaskRecordDetail();
        PageInfo pageInfo = new PageInfo(ListTaskRecordDetail);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据用户返回所有安装组detail，（除去status为初始化、已计划和质检完成的task_record) ，其中不限于包括：
     * "machine_id":"", 	-->machine.machine_id
     * "task_name":"",	-->task_record.task_name
     * "status":"",		-->task_record.status
     * "交货日期":"",		-->machine_order.contract_ship_date
     * "计划日期":"",		-->machine_order.plan_ship_date
     *
     * @param userAccount
     * @return
     */
    @PostMapping("selectAllInstallTaskRecordDetailByUserAccount")
    public Result selectAllInstallTaskRecordDetailByUserAccount(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                                                @RequestParam String userAccount) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> ListTaskRecordDetail = taskRecordService.selectAllInstallTaskRecordDetailByUserAccount(userAccount);
        PageInfo pageInfo = new PageInfo(ListTaskRecordDetail);
        if (ListTaskRecordDetail.isEmpty()) {
            return ResultGenerator.genSuccessResult("Empty Result");
        } else {
            return ResultGenerator.genSuccessResult(pageInfo);
        }
    }

    /**
     * 根据用户返回所有检测员detail，其中不限于包括：
     * "machine_id":"", 	-->machine.machine_id
     * "task_name":"",	-->task_record.task_name
     * "status":"",		-->task_record.status
     * "交货日期":"",		-->machine_order.contract_ship_date
     * "计划日期":"",		-->machine_order.plan_ship_date
     *
     * @param page
     * @param size
     * @param userAccount
     * @return
     */
    @PostMapping("selectAllQaTaskRecordDetailByUserAccount")
    public Result selectAllQaTaskRecordDetailByUserAccount(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                                           @RequestParam String userAccount) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> ListTaskRecordDetail = taskRecordService.selectAllQaTaskRecordDetailByUserAccount(userAccount);
        PageInfo pageInfo = new PageInfo(ListTaskRecordDetail);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //根据机器的流程记录ID，返回未计划的作业任务（task plan）
    @PostMapping("/selectNotPlanedTaskRecord")
    public Result selectNotPlanedTaskRecord(@RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "0") Integer size,
                                            Integer processRecordID) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.selectNotPlanedTaskRecord(processRecordID);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 获取已计划的task record
     */
    @PostMapping("/selectPlanedTaskRecords")
    public Result selectPlanedTaskRecords(@RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "0") Integer size,
                                          String orderNum,
                                          String machineStrId,
                                          String taskName,
                                          String nameplate,
                                          Integer installStatus,
                                          Integer machineType,
                                          String query_start_time,
                                          String query_finish_time,
                                          @RequestParam(defaultValue = "true") Boolean is_fuzzy) {

        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectPlanedTaskRecords(orderNum, machineStrId, taskName, nameplate, installStatus, machineType, query_start_time, query_finish_time, is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/getTaskRecordData")
    public Result getTaskRecordData(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") Integer id,
            @RequestParam(defaultValue = "0") Integer processRecordId
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.getTaskRecordData(id, processRecordId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/updateStatus")
    @Transactional(rollbackFor = Exception.class)
    public Result updateStatus(String taskRecord, String processRecord) {
        TaskRecord tr = JSON.parseObject(taskRecord, TaskRecord.class);
        Integer id = tr.getId();
        if (id == null || id < 0) {
            return ResultGenerator.genFailResult("TaskRecord的ID为空，数据更新失败！");
        }
        taskRecordService.update(tr);

        ProcessRecord pr = JSON.parseObject(processRecord, ProcessRecord.class);
        id = pr.getId();
        if (id == null || id < 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultGenerator.genFailResult("ProcessRecord的ID为空，数据更新失败！");
        }
        processRecordService.update(pr);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/updateTaskInfo")
    @Transactional(rollbackFor = Exception.class)
    public Result updateTaskInfo(String taskRecord) {
        TaskRecord tr = JSON.parseObject(taskRecord, TaskRecord.class);
        Integer id = tr.getId();
        if (id == null || id < 0) {
            return ResultGenerator.genFailResult("TaskRecord的ID为空，数据更新失败！");
        }

//        if (tr.getStatus() == 0 && tr.getLeader() == "" && tr.getWorkerList() == "") {
//            return ResultGenerator.genFailResult("无数据可更新！");
//        }
        taskRecordService.update(tr);

        Integer prId = tr.getProcessRecordId();
        if (prId == null || prId < 0) {
            Logger.getLogger("").log(Level.INFO, "processrecord Id 为空");
        } else {
            ProcessRecord pr = processRecordService.findById(prId);
            Machine machine = machineService.findById(pr.getMachineId());
            boolean isNeedUpdateMachine = false;
            if (pr != null) {
                String nodeData = pr.getNodeData();
                List<NodeDataModel> ndList = JSON.parseArray(nodeData, NodeDataModel.class);
                NodeDataModel ndItem = null;
                Integer index = -1;
                Boolean isFinished = true;
                for (int i = 0; i < ndList.size(); i++) {
                    if (Integer.parseInt(ndList.get(i).getKey()) == tr.getNodeKey()) {
                        index = i;
                    }
                    if (ndList.get(i).getTaskStatus() != null
                            && Integer.parseInt(ndList.get(i).getTaskStatus()) < Constant.TASK_QUALITY_DONE) {
                        isFinished = false;
                    }
                }
                if (index > -1) {
                    ndItem = ndList.get(index);
                    ndItem.setTaskStatus(tr.getStatus().toString());
                    if (tr.getInstallBeginTime() != null) {
                        String date = Utils.getFormatStringDate(tr.getInstallBeginTime(), "yyyy-MM-dd HH:mm:ss");
                        ndItem.setBeginTime(date);
                    }
                    if (tr.getQualityEndTime() != null) {
                        String date = Utils.getFormatStringDate(tr.getQualityEndTime(), "yyyy-MM-dd HH:mm:ss");
                        ndItem.setEndTime(date);//质检完成，工序才算完成
                    }
                    if (tr.getLeader() != null && tr.getLeader().length() > 0) {
                        ndItem.setLeader(tr.getLeader());//组长信息
                    }
                    if (tr.getWorkerList() != null && tr.getWorkerList().length() > 0) {
                        ndItem.setWorkList(tr.getWorkerList());//工作人员信息
                    }
                    ndList.set(index, ndItem);
                }
                if (isFinished && tr.getStatus() >= Constant.TASK_QUALITY_DONE.intValue()) {//所有工序完成
                    pr.setEndTime(new Date());
                    //安装完成
                    machine.setStatus(Constant.MACHINE_INSTALLED);
                    isNeedUpdateMachine=true;
                }
                pr.setNodeData(JSON.toJSONString(ndList));
                processRecordService.update(pr);

                if (machine.getStatus().equals(Constant.MACHINE_PLANING)) {
                    //安装中
                    machine.setStatus(Constant.MACHINE_INSTALLING);
                    isNeedUpdateMachine=true;
                }
                if(isNeedUpdateMachine) {
                    machine.setUpdateTime(new Date());
                    machineService.update(machine);
                }

            }
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据机器铭牌（即机器编号）查询对应的机器正在操作的taskRecordDetail(全部状态) 。
     *
     * @param page
     * @param size
     * @param namePlate
     * @return
     */
    //TODO: 该接口返回的数据似乎不完整，待查。
    @PostMapping("/selectTaskRecordByMachineNameplate")
    public Result selectTaskRecordByMachineNameplate(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String namePlate
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectTaskRecordByMachineNameplate(namePlate);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据机器的系统编号（machine_strid）查询对应的机器正在操作的taskRecordDetail(全部状态)。
     *
     * @param page
     * @param size
     * @param machineStrId
     * @return
     */
    @PostMapping("/selectTaskRecordByMachineStrId")
    public Result selectTaskRecordByMachineStrId(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String machineStrId
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectTaskRecordByMachineStrId(machineStrId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据account和机器的系统编号（machine_strid），
     * 返回对应机器正在操作的步骤（除去status为初始化、已计划和质检完成的task_record），且属于该account的排班计划。
     *
     * @param page
     * @param size
     * @param machineStrId
     * @param account
     * @return
     */
    @PostMapping("/selectTaskRecordByMachineStrIdAndAccount")
    public Result selectTaskRecordByMachineStrIdAndAccount(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String machineStrId,
            @RequestParam(defaultValue = "0") String account
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectTaskRecordByMachineStrIdAndAccount(machineStrId, account);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 返回待计划的Task_record具体信息
     *
     * @param page
     * @param size
     * @param machineStrId
     * @param account
     * @return
     */
    @PostMapping("/selectUnPlannedTaskRecordByMachineStrIdAndAccount")
    public Result selectUnPlannedTaskRecordByMachineStrIdAndAccount(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String machineStrId,
            @RequestParam(defaultValue = "0") String account
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectUnPlannedTaskRecordByMachineStrIdAndAccount(machineStrId, account);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据account返回该用户的的待计划安装任务
     *
     * @param page
     * @param size
     * @param account
     * @return
     */
    @PostMapping("/selectUnplannedTaskRecordByAccount")
    public Result selectUnplandTaskRecordByAccount(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String account
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectUnplannedTaskRecordByAccount(account);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 返回满足user+machine_strId且处于安装完成待质检和质检异常状态的质检任务
     *
     * @param page
     * @param size
     * @param machineStrId
     * @param account
     * @return
     */
    @PostMapping("selectQATaskRecordDetailByAccountAndMachineStrID")
    public Result selectQATaskRecordDetailByAccountAndMachineStrID(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String machineStrId,
            @RequestParam(defaultValue = "0") String account
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectQATaskRecordDetailByAccountAndMachineStrID(machineStrId, account);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
