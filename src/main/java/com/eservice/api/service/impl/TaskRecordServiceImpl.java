package com.eservice.api.service.impl;

import com.eservice.api.dao.TaskRecordMapper;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
import com.eservice.api.service.TaskRecordService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
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
    public List<TaskRecord> selectTaskPlans(Integer taskRecordId) {
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

}
