package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.order_loading_list.OrderLoadingList;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
import com.eservice.api.service.impl.TaskRecordServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/01.
*/
@RestController
@RequestMapping("/task/record")
public class TaskRecordController {
    @Resource
    private TaskRecordServiceImpl taskRecordService;

    @PostMapping("/add")
    public Result add(String taskRecord) {
        TaskRecord taskRecord1 = JSON.parseObject(taskRecord,TaskRecord.class);
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
        TaskRecord taskRecord1 = JSON.parseObject(taskRecord,TaskRecord.class);
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
     *  根据userAccount 返回该用户的 Tasks
     * @param page
     * @param size
     * @param userAccount
     * @return 返回该用户的 Tasks
     */
    @PostMapping("/selectTaskReocords")
    public  Result selectTaskReocords(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                               @RequestParam String userAccount) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.selectTaskReocords(userAccount);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     *  根据 taskRecord.id 返回 task_plans
     * @param page
     * @param size
     * @param taskRecordId
     * @return
     */
    @PostMapping("/selectTaskPlans")
    public  Result selectTasks(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                               @RequestParam Integer taskRecordId) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.selectTaskPlans(taskRecordId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据  taskRecord.id 返回processRecord, machine，machine order,order_loading_list 信息。
     * @param taskRecordId
     * @return
     */
    @PostMapping("selectTaskRecordDetail")
    public Result selectTaskRecordDetail( @RequestParam Integer taskRecordId) {
        TaskRecordDetail taskRecordDetail = taskRecordService.selectTaskRecordDetail(taskRecordId);
        return ResultGenerator.genSuccessResult(taskRecordDetail);
    }

    /**
     * 给生产部管理员返回所有detail，其中不限于包括：
     * {
     "machine_id":"",
     "task_name":"",
     "status":"",
     "交货日期":"",
     "计划日期":"",
     }
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


}
