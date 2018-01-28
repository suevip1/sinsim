package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.service.impl.ProcessRecordServiceImpl;
import com.eservice.api.service.impl.TaskRecordServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public Result update(ProcessRecord processRecord) {
        processRecordService.update(processRecord);
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
    public Result addProcessForMachine(String taskRecords, String processRecord) {
        ProcessRecord pr = JSON.parseObject(processRecord, ProcessRecord.class);
        List<TaskRecord> trList = JSON.parseArray(taskRecords, TaskRecord.class);

        if (pr == null || trList == null) {
            return ResultGenerator.genFailResult("保存对象JSON解析失败！");
        }

        pr.setCreateTime(new Date());
        processRecordService.save(pr);
        Integer prId = pr.getId();
        trList.forEach((item) -> {
            item.setProcessRecordId(prId);
        });
        try {

            taskRecordService.save(trList);

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultGenerator.genFailResult("保存失败！");
        }
        return ResultGenerator.genSuccessResult();
    }
}
