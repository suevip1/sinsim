package com.eservice.api.service.impl;

import com.eservice.api.dao.QualityRecordImageMapper;
import com.eservice.api.model.quality_record_image.QualityRecordImage;
import com.eservice.api.service.QualityRecordImageService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/06.
*/
@Service
@Transactional
public class QualityRecordImageServiceImpl extends AbstractService<QualityRecordImage> implements QualityRecordImageService {
    @Resource
    private QualityRecordImageMapper qualityRecordImageMapper;

}
