package com.eservice.api.service.impl;

import com.eservice.api.dao.TaskRecordMapper;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
import com.eservice.api.model.task_record.TaskRecordExpired;
import com.eservice.api.model.task_record.TaskReport;
import com.eservice.api.model.user.User;
import com.eservice.api.service.TaskRecordService;
import com.eservice.api.core.AbstractService;
import com.eservice.api.service.common.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/12/01.
 */
@Service
@Transactional
public class TaskRecordServiceImpl extends AbstractService<TaskRecord> implements TaskRecordService {
    @Resource
    private TaskRecordMapper taskRecordMapper;
    @Resource
    private UserServiceImpl userService;

    public List<TaskRecord> selectTaskReocords(String userAccount) {
        return taskRecordMapper.selectTaskReocords(userAccount);
    }

    public List<TaskPlan> selectTaskPlans(Integer taskRecordId) {
        return taskRecordMapper.selectTaskPlans(taskRecordId);
    }

    public TaskRecordDetail selectTaskRecordDetail(Integer taskRecordId) {
        return taskRecordMapper.selectTaskRecordDetail(taskRecordId);
    }

    public List<TaskRecordDetail>  searchTaskRecordDetail(Integer taskRecordId,
                                                          String taskName,
                                                          String machineOrderNumber,
                                                          String queryStartTime,
                                                          String queryFinishTime,
                                                          String nameplate) {
        return taskRecordMapper.searchTaskRecordDetail(taskRecordId, taskName, machineOrderNumber, queryStartTime, queryFinishTime, nameplate);
    }

    public List<TaskRecordDetail> selectAllTaskRecordDetail() {
        return taskRecordMapper.selectAllTaskRecordDetail();
    }

    public List<TaskRecordDetail> selectAllInstallTaskRecordDetailByUserAccount(String userAccount) {
        User user = userService.selectByAccount(userAccount);
        /**
         * 对于出厂检测组，如果机器工序包含了未处理的异常，就不要显示。避免出厂检测把未完成的机型流出厂。
         */
        if (user.getGroupId() == 14) {// 14是出厂检验组
            return taskRecordMapper.selectAllInstallTaskRecordDetailByUserAccountChuChangJianCe(userAccount);
        } else {
            return taskRecordMapper.selectAllInstallTaskRecordDetailByUserAccount(userAccount);
        }
    }

    public List<TaskRecordDetail> selectAllQaTaskRecordDetailByUserAccount(String userAccount) {
        return taskRecordMapper.selectAllQaTaskRecordDetailByUserAccount(userAccount);
    }


    public List<TaskRecord> selectNotPlanedTaskRecord(Integer processRecordID) {
        return taskRecordMapper.selectNotPlanedTaskRecord(processRecordID);
    }

    public List<TaskRecord> getTaskRecordData(Integer id, Integer processRecordId) {
        return taskRecordMapper.getTaskRecordData(id, processRecordId);
    }

    public List<TaskRecordDetail> selectTaskRecordByMachineNameplate(String namePlate){
        return taskRecordMapper.selectTaskRecordByMachineNameplate(namePlate);
    }

    public List<TaskRecordDetail> selectTaskRecordByNamePlate(String namePlate){
        return taskRecordMapper.selectTaskRecordByNamePlate(namePlate);
    }

    public List<TaskRecordDetail> selectTaskRecordByNamePlateAndAccount(String namePlate, String account){
        return taskRecordMapper.selectTaskRecordByNamePlateAndAccount(namePlate, account);
    }
    public List<TaskRecordDetail> selectUnPlannedTaskRecordByNamePlateAndAccount(String namePlate, String account){
        return taskRecordMapper.selectUnPlannedTaskRecordByNamePlateAndAccount(namePlate, account);
    }

    public List<TaskRecordDetail> selectQATaskRecordDetailByAccountAndNamePlate(String namePlate, String account){
        return taskRecordMapper.selectQATaskRecordDetailByAccountAndNamePlate(namePlate, account);
    }

    public List<TaskRecordDetail> selectUnplannedTaskRecordByAccount(String account) {
        return taskRecordMapper.selectUnplannedTaskRecordByAccount(account);
    }

    public void deleteTaskRecordByCondition(Integer id, Integer processRecordId, Byte nodeKey, Byte status) {
        taskRecordMapper.deleteTaskRecordByCondition(id, processRecordId, nodeKey, status);
    }

    public List<TaskRecordDetail> selectPlanedTaskRecords(String orderNum, String machineStrId, String taskName, String nameplate, Integer installStatus, Integer machineType, String query_start_time, String query_finish_time, Boolean is_fuzzy) {
        if (is_fuzzy) {
            return taskRecordMapper.selectPlanedTaskRecordsByFuzzy(orderNum, machineStrId, taskName, nameplate, installStatus, machineType, query_start_time, query_finish_time);
        } else {
            return taskRecordMapper.selectPlanedTaskRecords(orderNum, machineStrId, taskName, nameplate, installStatus, machineType, query_start_time, query_finish_time);
        }
    }

    public List<TaskRecordExpired> getExpiredTaskStatistics(Integer mode) {
        return taskRecordMapper.getExpiredTaskStatistics(mode);
    }

    public List<TaskReport> selectTaskReports(String taskName, String installStartTime, String installFinishTime) {
        return taskRecordMapper.selectTaskReports(taskName, installStartTime, installFinishTime);
    }

    public List<TaskRecordDetail> selectMachineOrderStructureTable(  String queryMachineOrderCreateTime, //订单录入时间
                                                                     String orderNum,
                                                                     String saleMan,
                                                                     String machineType,     ///机器类型
                                                                     String nameplate,
                                                                     String needleNum,
                                                                     String headNum,
                                                                     String electricTrim,    ///剪线方式
                                                                     String TaskRecordStatus, ///工序状态
                                                                     String taskRecordEndTime,///工序完成(结束)时间
                                                                     String queryStartTimePlanShipDate,
                                                                     String queryFinishTimePlanShipDate,
                                                                     String taskName ) {
        return taskRecordMapper.selectMachineOrderStructureTable(
                queryMachineOrderCreateTime, //订单录入时间
                orderNum,
                saleMan,
                machineType,     ///机器类型
                nameplate,
                needleNum,
                headNum,
                electricTrim,    ///剪线方式
                TaskRecordStatus, ///工序状态
                taskRecordEndTime,///工序完成(结束)时间
                queryStartTimePlanShipDate,
                queryFinishTimePlanShipDate,
                taskName);
    }


}
