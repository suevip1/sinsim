package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.service.impl.TaskPlanServiceImpl;
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
* @date 2017/11/14.
*/
@RestController
@RequestMapping("/task/plan")
public class TaskPlanController {
    @Resource
    private TaskPlanServiceImpl taskPlanService;

    @PostMapping("/add")
    public Result add(TaskPlan taskPlan) {
        taskPlanService.save(taskPlan);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        taskPlanService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(TaskPlan taskPlan) {
        taskPlanService.update(taskPlan);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        TaskPlan taskPlan = taskPlanService.findById(id);
        return ResultGenerator.genSuccessResult(taskPlan);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TaskPlan> list = taskPlanService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/addTaskPlans")
    public Result addTaskPlans(@RequestParam List<Integer> taskRecordIds, Integer planType, @RequestParam Date planDate, Integer userId){
        if(taskRecordIds == null || planType == null || planDate == null || userId == null) {
            return ResultGenerator.genFailResult("参数错误！");
        }else {
            boolean result = taskPlanService.addTaskPlans(taskRecordIds, planType, planDate, userId);
            if(result) {
                return ResultGenerator.genSuccessResult();
            }else {
                return ResultGenerator.genFailResult("作业可能已存在！");
            }
        }
    }
}
