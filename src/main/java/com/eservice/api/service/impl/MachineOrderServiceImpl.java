package com.eservice.api.service.impl;

import com.eservice.api.dao.MachineOrderMapper;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


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
    public List<MachineOrderDetail> selectOrder(Integer id, String contract_num, Integer status, String sellman,
                                                String customer, String query_start_time, String query_finish_time, String machine_name, Boolean is_fuzzy){
//        if (is_fuzzy){
            return  machineOrderMapper.selectOrderFuzzy(id, contract_num, status,sellman,customer,query_start_time,query_finish_time,machine_name);
//        } else {
//            return  machineOrderMapper.selectOrder(id, contract_num, status,saleman,customer,create_time,end_time,machine_name);
//        }
    }
}
