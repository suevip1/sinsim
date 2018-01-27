package com.eservice.api.service.impl;

import com.eservice.api.dao.ProcessMapper;
import com.eservice.api.model.process.Process;
import com.eservice.api.service.ProcessService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/11/14.
 */
@Service
@Transactional
public class ProcessServiceImpl extends AbstractService<Process> implements ProcessService {
    @Resource
    private ProcessMapper processMapper;

    public List<Process> selectProcess(
            Integer id,
            Boolean isSimple
    ) {
        if(isSimple) {
            return processMapper.selectProcessSimple(id);
        }
        return processMapper.selectProcess(id);
    }
}
