package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecord;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecordDetail;
import com.eservice.api.service.QualityInspectRecordService;
import com.eservice.api.service.impl.QualityInspectRecordServiceImpl;
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
    private QualityInspectRecordServiceImpl qualityInspectRecordService;

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

    /**
     *  查询质检记录
     * @param taskName      --工序名称
     * @param recordStatus  -- 质检状态（结果）
     * @param nameplate     --机器铭牌号
     * @param inspectName   --质检的名称
     * @param inspectPerson --质检人员名称
     * @param recordRemark  --该条质检的备注
     * @param reInspect     -- 复检结果
     * @param queryStartTime
     * @param queryFinishTime
     * @return
     */
    @PostMapping("selectQualityInspectRecordDetail")
    public Result selectQualityInspectRecordDetail(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                                   String orderNumber,
                                                   String taskName,
                                                   String recordStatus,
                                                   String nameplate,
                                                   String inspectName,
                                                   String inspectPerson,
                                                   String recordRemark,
                                                   String reInspect,
                                                   String queryStartTime,
                                                   String queryFinishTime ) {
        PageHelper.startPage(page, size);
        List<QualityInspectRecordDetail>  taskRecordDetailList = qualityInspectRecordService.selectQualityInspectRecordDetail(
                orderNumber,
                taskName,
                recordStatus,
                nameplate,
                inspectName,
                inspectPerson,
                recordRemark,
                reInspect,
                queryStartTime,
                queryFinishTime );
        PageInfo pageInfo = new PageInfo(taskRecordDetailList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
