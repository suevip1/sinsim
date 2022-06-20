package com.eservice.api.service.impl;

import com.eservice.api.dao.InstallPlanActualMapper;
import com.eservice.api.model.install_plan_actual.InstallPlanActual;
import com.eservice.api.model.install_plan_actual.InstallPlanActualDetails;
import com.eservice.api.service.InstallPlanActualService;
import com.eservice.api.core.AbstractService;
import com.eservice.api.service.common.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/07/19.
*/
@Service
@Transactional
public class InstallPlanActualServiceImpl extends AbstractService<InstallPlanActual> implements InstallPlanActualService {
    @Resource
    private InstallPlanActualMapper installPlanActualMapper;

    public List<InstallPlanActualDetails> selectInstallPlanActualDetails(String orderNum,
                                                                         String nameplate,
                                                                         String installGroupName,
                                                                         String type,
                                                                         String queryStartTime,
                                                                         String queryFinishTime,
                                                                         Boolean isNotFinished) {
        if(null != type && type.equals(Constant.STR_INSTALL_TYPE_WHOLE)) {
            return installPlanActualMapper.selectInstallPlanActualDetails(orderNum, nameplate, installGroupName, type, queryStartTime, queryFinishTime, isNotFinished);
        }else {
            return installPlanActualMapper.selectInstallPlanActualDetails_Parts(orderNum, nameplate, installGroupName, type, queryStartTime, queryFinishTime, isNotFinished);
        }
    }
    public InstallPlanActual getInstallPlanActual(Integer installPlanId){
        return installPlanActualMapper.getInstallPlanActual(installPlanId);
    }

    public List<InstallPlanActual> getInstallPlanActualList(Integer installPlanId){
        return installPlanActualMapper.getInstallPlanActualList(installPlanId);
    }

    public List<InstallPlanActualDetails> selectInstallPlanActualDetailsForShowingBoard(
            String queryStartTime,
            String queryFinishTime){
        return installPlanActualMapper.selectInstallPlanActualDetailsForShowingBoard( queryStartTime, queryFinishTime );
    }

    public List<InstallPlanActualDetails> selectInstallPlanActualDetailsForShowingBoard_Parts(
            String queryStartTime,
            String queryFinishTime){
        return installPlanActualMapper.selectInstallPlanActualDetailsForShowingBoard_Parts( queryStartTime, queryFinishTime );
    }
}
