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


    public List<QualityInspectRecordDetail> selectQualityInspectRecordDetail(String taskName,
                                                                             String recordStatus,
                                                                             String nameplate,
                                                                             String inspectName,
                                                                             String inspectPerson,
                                                                             String recordRemark,
                                                                             String reInspect,
                                                                             String queryStartTime,
                                                                             String queryFinishTime) {
        return qualityInspectRecordMapper.selectQualityInspectRecordDetail(
                taskName,
                recordStatus,
                nameplate,
                inspectName,
                inspectPerson,
                recordRemark,
                reInspect,
                queryStartTime,
                queryFinishTime );
    }

}
