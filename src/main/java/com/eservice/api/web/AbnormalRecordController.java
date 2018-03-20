package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.abnormal.Abnormal;
import com.eservice.api.model.abnormal_image.AbnormalImage;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.model.abnormal_record.AbnormalRecordDetail;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.service.AbnormalImageService;
import com.eservice.api.service.AbnormalService;
import com.eservice.api.service.impl.AbnormalRecordServiceImpl;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.eservice.api.service.impl.TaskRecordServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
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
* @date 2017/12/27.
*/
@RestController
@RequestMapping("/abnormal/record")
public class AbnormalRecordController {
    @Resource
    private AbnormalRecordServiceImpl abnormalRecordService;
    @Resource
    private AbnormalService abnormalService;
    @Resource
    private AbnormalImageService abnormalImageService;
    @Resource
    private TaskRecordServiceImpl taskRecordService;
    @Resource
    private MachineServiceImpl machineService;

    @PostMapping("/add")
    public Result add(String abnormalRecord) {
        AbnormalRecord abnormalRecord1 = JSON.parseObject(abnormalRecord,AbnormalRecord.class);

        abnormalRecordService.save(abnormalRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        abnormalRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String abnormalRecord) {
        AbnormalRecord abnormalRecord1 = JSON.parseObject(abnormalRecord, AbnormalRecord.class);
        abnormalRecordService.update(abnormalRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        AbnormalRecord abnormalRecord = abnormalRecordService.findById(id);
        return ResultGenerator.genSuccessResult(abnormalRecord);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AbnormalRecord> list = abnormalRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据 task_record.id 返回abnormalRecordDetail
     * @param taskRecordId
     * @return
     */
    @PostMapping("/selectAbnormalRecordDetails")
    public Result selectAbnormalRecordDetails(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                       @RequestParam Integer taskRecordId) {
        PageHelper.startPage(page, size);
        List<AbnormalRecordDetail> abnormalRecordDetailList = abnormalRecordService.selectAbnormalRecordDetails(taskRecordId);
        PageInfo pageInfo = new PageInfo(abnormalRecordDetailList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据异常类型、异常提交时间、提交者、解决者，返回abnormalRecordDetail
     * @return
     */
    @PostMapping("/selectAbnormalRecordDetailList")
    public Result selectAbnormalRecordDetailList(@RequestParam(defaultValue = "0") Integer page,
                                                 @RequestParam(defaultValue = "0") Integer size,
                                                 Integer abnormalType,
                                                 String taskName,
                                                 Integer submitUser,
                                                 Integer solutionUser,
                                                 Date queryStartTime,
                                                 Date queryFinishTime) {
        PageHelper.startPage(page, size);
        List<AbnormalRecordDetail> abnormalRecordDetailList = abnormalRecordService.selectAbnormalRecordDetailList(abnormalType,taskName, submitUser, solutionUser, queryStartTime, queryFinishTime);
        PageInfo pageInfo = new PageInfo(abnormalRecordDetailList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    /**
     *根据传入的strAbnormalRecordDetail，更新对应多表：
     "machine_id":"", --> machine.machine_id
     "安装是否异常":"", --> task_record.status  task状态，“1”==>未开始， “2”==>进行中，“3”==>安装完成， “4”==>质检完成，“5“===>异常
     "异常类型":"",	--> abnormal_record.abnormal_type
     "异常原因":"", --> abnormal_record.comment
     "异常照片":"", -->abnormal_image.image
     "安装完成":"",  -->   task_record.status或machine.status都可以，反正这两个表都更新
     注意：有外键的字段，需要上传实际存在的外键数据。
     一项update失败的情况下，全部update无效(事务OK)
     * @param strAbnormalRecordDetail
     * @return
     */
    @PostMapping("/updateAbnormalRecordDetail")
    @Transactional
    public Result updateAbnormalRecordDetail(@RequestParam String strAbnormalRecordDetail) {
        //获取整体detail
        AbnormalRecordDetail abnormalRecordDetail1 = JSON.parseObject(strAbnormalRecordDetail, AbnormalRecordDetail.class);

        Integer abnormalRecordDetail_ID = abnormalRecordDetail1.getId();

        AbnormalRecord abnormalRecord = abnormalRecordService.findById(abnormalRecordDetail_ID);
        abnormalRecord.setAbnormalType(abnormalRecordDetail1.getAbnormalType());
        abnormalRecord.setTaskRecordId(abnormalRecordDetail1.getTaskRecordId());
        abnormalRecord.setSubmitUser(abnormalRecordDetail1.getSubmitUser());
        abnormalRecord.setSolutionUser(abnormalRecordDetail1.getSolutionUser());
        abnormalRecord.setComment(abnormalRecordDetail1.getComment());
        abnormalRecord.setSolution(abnormalRecordDetail1.getSolution());

        Abnormal abnormal = abnormalRecordDetail1.getAbnormal();
        AbnormalImage abnormalImage = abnormalRecordDetail1.getAbnormalImage();
        TaskRecord taskRecord = abnormalRecordDetail1.getTaskRecord();
        Machine machine = abnormalRecordDetail1.getMachine();

        abnormalRecordService.update(abnormalRecord);
        abnormalService.update(abnormal);
        abnormalImageService.update(abnormalImage);
        taskRecordService.update(taskRecord);
        machineService.update(machine);

        return ResultGenerator.genSuccessResult();
    }
}
