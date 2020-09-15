package com.eservice.api.service.impl;

import com.eservice.api.core.AbstractService;
import com.eservice.api.dao.OptimizeTestMapper;
import com.eservice.api.model.optimizeTest.OptimizeTest;
import com.eservice.api.service.OptimizeTestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2020/07/07.
*/
@Service
@Transactional
public class OptimizeTestServiceImpl extends AbstractService<OptimizeTest> implements OptimizeTestService {
    @Resource
    private OptimizeTestMapper optimizeTestMapper;

    public List<OptimizeTest> selectOptimizeList(
            String projectName,
            String optimizePart,
            String orderNum,
            String queryStartTimeCreate,
            String queryFinishTimeCreate,
            String machineType,
            String purpose,
            String owner,
            String queryStartTimeUpdate,
            String queryFinishTimeUpdate
    ){
        return optimizeTestMapper.selectOptimizeList(
                projectName,
                optimizePart,
                orderNum,
                queryStartTimeCreate,
                queryFinishTimeCreate,
                machineType,
                purpose,
                owner,
                queryStartTimeUpdate,
                queryFinishTimeUpdate
        );
    }


    public void saveAndGetID(OptimizeTest optimizeTest){
        optimizeTestMapper.saveAndGetID(optimizeTest);
    }

}
