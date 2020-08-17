package com.eservice.api.service.impl;

import com.eservice.api.dao.QualityInspectMapper;
import com.eservice.api.model.quality_inspect.QualityInspect;
import com.eservice.api.service.QualityInspectService;
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
public class QualityInspectServiceImpl extends AbstractService<QualityInspect> implements QualityInspectService {
    @Resource
    private QualityInspectMapper qualityInspectMapper;

}
