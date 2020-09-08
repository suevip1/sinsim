package com.eservice.api.web;

import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.task.Task;
import com.eservice.api.service.TaskService;
import com.eservice.api.service.impl.TaskServiceImpl;
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
 *
 * @author Wilson Hu
 * @date 2017/12/18.
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskServiceImpl taskService;

    @PostMapping("/add")
    public Result add(Task task) {
        taskService.save(task);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        taskService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Task task) {
        if (task.getQualityUserId() == null) {
            task.setQualityUserId(0);
        }
        taskService.update(task);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Task task = taskService.findById(id);
        return ResultGenerator.genSuccessResult(task);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Task> list = taskService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据铭牌号查找该机器的所有工序的名称，
     * 可以用于创建与该工序对应的质检记录（状态为未质检）
     * @param nameplate
     * @return
     */
    @PostMapping("/getTaskByNameplate")
    public Result getTaskByNameplate(@RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "0") Integer size,
                                     @RequestParam String nameplate) {
        PageHelper.startPage(page, size);
        List<Task> list = taskService.getTaskByNameplate(nameplate);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
