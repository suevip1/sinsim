package com.eservice.api.service.impl;

import com.eservice.api.core.AbstractService;
import com.eservice.api.dao.MachineOrderMapper;
import com.eservice.api.model.contact_form.ContactForm;
import com.eservice.api.model.contact_form.ContactFormDetail;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.service.common.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/11/24.
 */
@Service
@Transactional
public class MachineOrderServiceImpl extends AbstractService<MachineOrder> implements MachineOrderService {
    @Resource
    private MachineOrderMapper machineOrderMapper;

    @Resource
    private ContactFormServiceImpl contactFormService;

    public MachineOrderDetail getOrderAllDetail(Integer id) {
        return machineOrderMapper.getOrderAllDetail(id);
    }

    public List<MachineOrderDetail> selectOrder(Integer id, Integer contract_id, String order_num, String contract_num, Integer status, String sellman,
                                                String customer, String marketGroupName, String query_start_time, String query_finish_time, String machine_name, Boolean is_fuzzy) {

        List<MachineOrderDetail> machineOrderDetailList;
        /**
         * Note: 对每个订单都查询一次该订单的联系单状态，然后赋值。
         */
        if (is_fuzzy) {
            machineOrderDetailList = machineOrderMapper.selectOrderFuzzy(id, contract_id, order_num, contract_num, status, sellman, customer, marketGroupName, query_start_time, query_finish_time, machine_name);
            for (int i = 0; i < machineOrderDetailList.size(); i++) {
                machineOrderDetailList.get(i).setContactFormDetailList(relatedLxdPassed(machineOrderDetailList.get(i).getOrderNum()));

            }
        } else {
            machineOrderDetailList = machineOrderMapper.selectOrder(id, contract_id, order_num, contract_num, status, sellman, customer, marketGroupName, query_start_time, query_finish_time, machine_name);
            for (int i = 0; i < machineOrderDetailList.size(); i++) {
                machineOrderDetailList.get(i).setContactFormDetailList(relatedLxdPassed(machineOrderDetailList.get(i).getOrderNum()));
            }
        }
        return machineOrderDetailList;
    }

    /**
     * 订单对应的联系单是否已经存在并且通过审核了
     */
    public List<ContactFormDetail> relatedLxdPassed(String orderNum) {
        List<ContactFormDetail> contactFormDetailsList = contactFormService.selectContacts(null,
                orderNum,
                null,
                null,
                null,// 可能有多个联系单，所以不能用 Constant.STR_LXD_CHECKING_FINISHED,
                null,
                null,
                null,
                null);
        return contactFormDetailsList;

//        if(contactFormDetailsList !=null && contactFormDetailsList.size() !=0){
//            //有多个联系单，取最后一个为准
//            if(contactFormDetailsList.get(contactFormDetailsList.size() -1).getStatus().equals(Constant.STR_LXD_CHECKING_FINISHED)){
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
    }

    public void saveAndGetID(MachineOrder machineOrder) {
        machineOrderMapper.saveAndGetID(machineOrder);
    }

    public MachineOrder searchOrderIdByOrderLoadingListId(Integer ollId) {
        return machineOrderMapper.searchOrderIdByOrderLoadingListId(ollId);
    }

    public Integer getUsedMachineTypeCount(Integer machineTypeId) {
        return machineOrderMapper.getUsedMachineTypeCount(machineTypeId).get(0);//SQL查询出来的结构是List<Integer>,第一个元素就是查询出来的count
    }

    public MachineOrder getMachineOrder(String orderNum) {
        return machineOrderMapper.getMachineOrder(orderNum);
    }

}
