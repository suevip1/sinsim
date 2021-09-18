package com.eservice.api.service.impl;

import com.eservice.api.dao.MachineTypeMapper;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.service.MachineTypeService;
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
public class MachineTypeServiceImpl extends AbstractService<MachineType> implements MachineTypeService {
    @Resource
    private MachineTypeMapper machineTypeMapper;
    public List<MachineType> selectByName(String name)
    {
        return machineTypeMapper.selectByName(name);
    }

    //查询第n条之后的记录（目前n为23，返回从24开始，1-23不再用了）
    public List<MachineType> selectAfterN(int nn)
    {
        return machineTypeMapper.selectAfterN(nn);
    }
}
