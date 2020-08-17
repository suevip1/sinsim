package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecord;
import com.eservice.api.service.QualityInspectRecordService;
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
* @date 2020/08/17.
*/
@RestController
@RequestMapping("/quality/inspect/record")
public class QualityInspectRecordController {
    @Resource
    private QualityInspectRecordService qualityInspectRecordService;

    @PostMapping("/add")
    public Result add(QualityInspectRecord qualityInspectRecord) {
        qualityInspectRecordService.save(qualityInspectRecord);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        qualityInspectRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(QualityInspectRecord qualityInspectRecord) {
        qualityInspectRecordService.update(qualityInspectRecord);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        QualityInspectRecord qualityInspectRecord = qualityInspectRecordService.findById(id);
        return ResultGenerator.genSuccessResult(qualityInspectRecord);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<QualityInspectRecord> list = qualityInspectRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
