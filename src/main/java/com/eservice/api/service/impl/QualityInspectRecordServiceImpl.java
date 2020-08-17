package com.eservice.api.service.impl;

import com.eservice.api.dao.QualityInspectRecordMapper;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecord;
import com.eservice.api.service.QualityInspectRecordService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


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

}
