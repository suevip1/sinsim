package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.model.abnormal_record.AbnormalRecordDetail;
import com.eservice.api.service.impl.AbnormalRecordServiceImpl;
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
* @date 2017/11/14.
*/
@RestController
@RequestMapping("/abnormal/record")
public class AbnormalRecordController {
    @Resource
    private AbnormalRecordServiceImpl abnormalRecordService;

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
     * ¸ù¾Ý task_record.id ·µ»ØabnormalRecordDetail
     * @param taskRecordId
     * @return
     */
    @PostMapping("/selectAbnormalRecordDetail")
    public Result selectAbnormalRecord(@RequestParam Integer taskRecordId) {
        AbnormalRecordDetail abnormalRecordDetail = abnormalRecordService.selectAbnormalRecordDetail(taskRecordId);
        return ResultGenerator.genSuccessResult(abnormalRecordDetail);
    }
}
