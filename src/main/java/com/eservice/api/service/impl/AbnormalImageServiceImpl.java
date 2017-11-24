package com.eservice.api.service.impl;

import com.eservice.api.dao.AbnormalImageMapper;
import com.eservice.api.model.abnormal_image.AbnormalImage;
import com.eservice.api.service.AbnormalImageService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/14.
*/
@Service
@Transactional
public class AbnormalImageServiceImpl extends AbstractService<AbnormalImage> implements AbnormalImageService {
    @Resource
    private AbnormalImageMapper abnormalImageMapper;

}
