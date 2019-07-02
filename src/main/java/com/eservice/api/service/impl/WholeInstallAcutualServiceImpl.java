package com.eservice.api.service.impl;

import com.eservice.api.dao.WholeInstallAcutualMapper;
import com.eservice.api.model.whole_install_acutual.WholeInstallAcutual;
import com.eservice.api.model.whole_install_acutual.WholeInstallAcutualDetails;
import com.eservice.api.service.WholeInstallAcutualService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/06/22.
*/
@Service
@Transactional
public class WholeInstallAcutualServiceImpl extends AbstractService<WholeInstallAcutual> implements WholeInstallAcutualService {
    @Resource
    private WholeInstallAcutualMapper wholeInstallAcutualMapper;

    public List<WholeInstallAcutualDetails> selectWholeInstallDetails(String orderNum,String nameplate,String installGroupName){
        return wholeInstallAcutualMapper.selectWholeInstallDetails(orderNum,nameplate,installGroupName);
    }

}
