package com.eservice.api.service.impl;

import com.eservice.api.dao.QualityInspectTotalMapper;
import com.eservice.api.model.quality_inspect_total.QualityInspectTotal;
import com.eservice.api.service.QualityInspectTotalService;
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
public class QualityInspectTotalServiceImpl extends AbstractService<QualityInspectTotal> implements QualityInspectTotalService {
    @Resource
    private QualityInspectTotalMapper qualityInspectTotalMapper;

}
