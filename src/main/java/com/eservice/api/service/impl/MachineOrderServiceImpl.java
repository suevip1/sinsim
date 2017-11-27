package com.eservice.api.service.impl;

import com.eservice.api.dao.MachineOrderMapper;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/24.
*/
@Service
@Transactional
public class MachineOrderServiceImpl extends AbstractService<MachineOrder> implements MachineOrderService {
    @Resource
    private MachineOrderMapper machineOrderMapper;

    public MachineOrderDetail getOrderAllDetail(Integer id) {
        return  machineOrderMapper.getOrderAllDetail(id);
    }
}
