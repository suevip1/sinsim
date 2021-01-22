package com.eservice.api.service.impl;

import com.eservice.api.dao.QualityInspectRecordMapper;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecord;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecordDetail;
import com.eservice.api.service.QualityInspectRecordService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2020/08/17.
*/
@Service
@Transactional
public class QualityInspectRecordServiceImpl extends AbstractService<QualityInspectRecord> implements QualityInspectRecordService {
    @Resource
    private QualityInspectRecordMapper qualityInspectRecordMapper;


    public List<QualityInspectRecordDetail> selectQualityInspectRecordDetail(
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
        String[] recordStatusArr;
        if(recordStatus == null || recordStatus.equals("")){
            recordStatusArr = null;
        } else {
            recordStatusArr = recordStatus.split(",");
        }
        return qualityInspectRecordMapper.selectQualityInspectRecordDetail(
                orderNumber,
                taskName,
                recordStatusArr,
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
    }

    public List<QualityInspectRecordDetail> selectQualityInspectRecordDetailGroupByMachine(
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
        return qualityInspectRecordMapper.selectQualityInspectRecordDetailGroupByMachine(
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
    }
}
