package com.eservice.api.service.impl;

import com.eservice.api.dao.IotMachineMapper;
import com.eservice.api.model.iot_machine.IotMachine;
import com.eservice.api.service.IotMachineService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2021/04/25.
*/
@Service
@Transactional
public class IotMachineServiceImpl extends AbstractService<IotMachine> implements IotMachineService {
    @Resource
    private IotMachineMapper iotMachineMapper;

}
