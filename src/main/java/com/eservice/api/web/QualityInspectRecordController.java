package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecord;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecordDetail;
import com.eservice.api.service.impl.QualityInspectRecordServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

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

    private Logger logger = Logger.getLogger(QualityInspectRecordController.class);

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

    /**
     *  更新质检记录
     *  接收的是JSON化的数组
     */
    @PostMapping("/updateQualityInspectRecordList")
    public Result updateQualityInspectRecordList(String mQualityInspectRecordTobeUploadList) {

        logger.info(mQualityInspectRecordTobeUploadList);
        JSONArray jsonArray = JSON.parseArray(mQualityInspectRecordTobeUploadList);

        if (null != jsonArray) {
            int size = jsonArray.size();
            logger.info("更新质检记录，size: " + size);
            for(int i=0; i<size; i++){
                QualityInspectRecord qualityInspectRecordTobeUpdate = JSON.parseObject((String) jsonArray.get(i).toString(), QualityInspectRecord.class);
                qualityInspectRecordTobeUpdate.setUpdateTime(new Date());
                qualityInspectRecordService.update(qualityInspectRecordTobeUpdate);
            }
        }
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
     * @param recordStatus  -- 质检状态（结果）--支持多个状态，以逗号分割
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
    public Result selectQualityInspectRecordDetail(@RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "0") Integer size,
                                                   String orderNumber,
                                                   String taskName,
                                                   String recordStatus,///支持多个状态用逗号隔开， "2,3,4"
                                                   String nameplate,
                                                   String inspectName,
                                                   String inspectType,
                                                   String inspectPhase,
                                                   String inspectContent,
                                                   String inspectPerson,
                                                   String recordRemark,
                                                   String reInspect,
                                                   String queryStartTime,
                                                   String queryFinishTime) {
        PageHelper.startPage(page, size);
        List<QualityInspectRecordDetail>  taskRecordDetailList = qualityInspectRecordService.selectQualityInspectRecordDetail(
                orderNumber,
                taskName,
                recordStatus,
                nameplate,
                inspectName,
                inspectType,
                inspectPhase,
                inspectContent,
                inspectPerson,
                recordRemark,
                reInspect,
                queryStartTime,
                queryFinishTime );
        PageInfo pageInfo = new PageInfo(taskRecordDetailList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("selectQualityInspectRecordDetailGroupByMachine")
    public Result selectQualityInspectRecordDetailGroupByMachine(@RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "0") Integer size,
                                                   String orderNumber,
                                                   String taskName,
                                                   String recordStatus,
                                                   String nameplate,
                                                   String inspectName,
                                                   String inspectType,
                                                   String inspectPhase,
                                                   String inspectContent,
                                                   String inspectPerson,
                                                   String recordRemark,
                                                   String reInspect,
                                                   String queryStartTime,
                                                   String queryFinishTime) {
        PageHelper.startPage(page, size);
        List<QualityInspectRecordDetail>  taskRecordDetailList = qualityInspectRecordService.selectQualityInspectRecordDetailGroupByMachine(
                orderNumber,
                taskName,
                recordStatus,
                nameplate,
                inspectName,
                inspectType,
                inspectPhase,
                inspectContent,
                inspectPerson,
                recordRemark,
                reInspect,
                queryStartTime,
                queryFinishTime );
        PageInfo pageInfo = new PageInfo(taskRecordDetailList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
