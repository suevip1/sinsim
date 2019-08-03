package com.eservice.api.service.impl;

import com.eservice.api.dao.InstallGroupMapper;
import com.eservice.api.model.install_group.InstallGroup;
import com.eservice.api.service.InstallGroupService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/14.
*/
@Service
@Transactional
public class InstallGroupServiceImpl extends AbstractService<InstallGroup> implements InstallGroupService {
    @Resource
    private InstallGroupMapper installGroupMapper;

    public List<InstallGroup> getInstallGroupByType(String type){
        return installGroupMapper.getInstallGroupByType(type);
    }
}
