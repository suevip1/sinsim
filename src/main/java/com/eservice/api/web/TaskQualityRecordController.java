package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.quality_record_image.QualityRecordImage;
import com.eservice.api.model.task_quality_record.TaskQualityRecord;
import com.eservice.api.model.task_quality_record.TaskQualityRecordDetail;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.service.impl.QualityRecordImageServiceImpl;
import com.eservice.api.service.impl.TaskQualityRecordServiceImpl;
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
* @date 2017/11/24.
*/
@RestController
@RequestMapping("/task/quality/record")
public class TaskQualityRecordController {
    @Resource
    private TaskQualityRecordServiceImpl taskQualityRecordService;
    @Resource
    private QualityRecordImageServiceImpl qualityRecordImageService;
    @Resource
    private TaskRecordServiceImpl taskRecordService;

    @PostMapping("/add")
    public Result add(String taskQualityRecord) {
        TaskQualityRecord taskQualityRecord1 = JSON.parseObject(taskQualityRecord,TaskQualityRecord.class);
        taskQualityRecordService.save(taskQualityRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        taskQualityRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String  taskQualityRecord) {
        TaskQualityRecord taskQualityRecord1 = JSON.parseObject(taskQualityRecord,TaskQualityRecord.class);
        taskQualityRecordService.update(taskQualityRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        TaskQualityRecord taskQualityRecord = taskQualityRecordService.findById(id);
        return ResultGenerator.genSuccessResult(taskQualityRecord);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TaskQualityRecord> list = taskQualityRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据  task_record.id 返回QualityRecordDetail，包括 qurlity_record_image,task_quality_record等
     * @param page
     * @param size
     * @param taskRecordId
     * @return
     */
    @PostMapping("/selectTaskQualityRecordDetails")
    public Result selectTaskQualityRecordDetail(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                       @RequestParam Integer taskRecordId) {
        PageHelper.startPage(page, size);
        List<TaskQualityRecordDetail> list = taskQualityRecordService.selectTaskQualityRecordDetails(taskRecordId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据传入的strTaskQualityRecordDetail，更新对应多表：
     "machine_id":"",  --> machine.machine_id
     "检验是否合格":"", --> task_quality_record.status 质检结果: "1"==>通过； “0”==>不通过
     "不合格原因":"",	--> task_quality_record.comment
     "不合格照片":"",	--> quality_record_image.image
     "检验完成":"",		--> task_record.status  task状态，“1”==>未开始， “2”==>进行中，“3”==>安装完成， “4”==>质检完成，“5“===>异常
     * @param strTaskQualityRecordDetail
     * @return
     */
    @PostMapping("updateTaskQualityRecordDetail")
    public Result updateTaskQualityRecordDetail(@RequestParam String strTaskQualityRecordDetail){
        TaskQualityRecordDetail taskQualityRecordDetail = JSON.parseObject(strTaskQualityRecordDetail,TaskQualityRecordDetail.class);
        Integer taskQualityRecordDetail_ID = taskQualityRecordDetail.getId();

        TaskQualityRecord taskQualityRecord = taskQualityRecordService.findById(taskQualityRecordDetail_ID);
        taskQualityRecord.setTaskRecordId(taskQualityRecordDetail.getTaskRecordId());
        taskQualityRecord.setName(taskQualityRecordDetail.getName());
        taskQualityRecord.setStatus(taskQualityRecordDetail.getStatus());
        taskQualityRecord.setCreateTime(taskQualityRecordDetail.getCreateTime());
        taskQualityRecord.setComment(taskQualityRecordDetail.getComment());

        QualityRecordImage qualityRecordImage = taskQualityRecordDetail.getQualityRecordImage();
        TaskRecord taskRecord = taskQualityRecordDetail.getTaskRecord();

        taskQualityRecordService.update(taskQualityRecord);
        qualityRecordImageService.update(qualityRecordImage);
        taskRecordService.update(taskRecord);
        return ResultGenerator.genSuccessResult();
    }
}
