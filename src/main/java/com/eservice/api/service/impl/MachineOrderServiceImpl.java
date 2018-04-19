package com.eservice.api.service.impl;

import com.eservice.api.core.AbstractService;
import com.eservice.api.dao.MachineOrderMapper;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.service.MachineOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    public List<MachineOrderDetail> selectOrder(Integer id, Integer contract_id, String order_num, String contract_num, Integer status, String sellman,
                                                String customer,String marketGroupName, String query_start_time, String query_finish_time, String machine_name, Boolean is_fuzzy){
        if (is_fuzzy){
            return  machineOrderMapper.selectOrderFuzzy(id, contract_id, order_num, contract_num, status,sellman, customer,marketGroupName, query_start_time, query_finish_time,machine_name);
        } else {
            return  machineOrderMapper.selectOrder(id, contract_id, order_num, contract_num, status, sellman, customer,marketGroupName, query_start_time, query_finish_time, machine_name);
        }
    }

    public void saveAndGetID(MachineOrder machineOrder){
        machineOrderMapper.saveAndGetID(machineOrder);
    }

    public MachineOrder searchOrderIdByOrderLoadingListId( Integer ollId){
        return machineOrderMapper.searchOrderIdByOrderLoadingListId(ollId);
    }
}
