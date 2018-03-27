package com.eservice.api.service.impl;

import com.eservice.api.dao.TaskRecordMapper;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
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

    public List<TaskRecord> selectTaskReocords(String userAccount) {
        return taskRecordMapper.selectTaskReocords(userAccount);
    }

    public List<TaskPlan> selectTaskPlans(Integer taskRecordId) {
        return taskRecordMapper.selectTaskPlans(taskRecordId);
    }

    public TaskRecordDetail selectTaskRecordDetail(Integer taskRecordId) {
        return taskRecordMapper.selectTaskRecordDetail(taskRecordId);
    }

    public List<TaskRecordDetail> selectAllTaskRecordDetail() {
        return taskRecordMapper.selectAllTaskRecordDetail();
    }

    public List<TaskRecordDetail> selectAllInstallTaskRecordDetailByUserAccount(String userAccount) {
        return taskRecordMapper.selectAllInstallTaskRecordDetailByUserAccount(userAccount);
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

    public List<TaskRecordDetail> selectUnplannedTaskRecordByAccount(String account){
        return taskRecordMapper.selectUnplannedTaskRecordByAccount(account);
    }

    public void deleteTaskRecordByCondition(Integer id, Integer processRecordId) {
        taskRecordMapper.deleteTaskRecordByCondition(id, processRecordId);
    }

    public List<TaskRecordDetail> selectPlanedTaskRecords(String orderNum, String machineStrId, String taskName, String nameplate, Integer installStatus, Integer machineType, String query_start_time, String query_finish_time, Boolean is_fuzzy){
        if(is_fuzzy){
            return taskRecordMapper.selectPlanedTaskRecordsByFuzzy(orderNum, machineStrId, taskName, nameplate,installStatus, machineType, query_start_time, query_finish_time);
        }else {
            return taskRecordMapper.selectPlanedTaskRecords(orderNum, machineStrId, taskName, nameplate,installStatus, machineType, query_start_time, query_finish_time);
        }
    }
}
