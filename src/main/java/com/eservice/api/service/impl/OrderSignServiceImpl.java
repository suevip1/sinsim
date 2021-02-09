package com.eservice.api.service.impl;

import com.eservice.api.dao.OrderSignMapper;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.service.OrderSignService;
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
public class OrderSignServiceImpl extends AbstractService<OrderSign> implements OrderSignService {
    @Resource
    private OrderSignMapper orderSignMapper;


    public List<OrderSign> getValidOrderSigns(Integer contractId) {
        return orderSignMapper.getValidOrderSigns(contractId);
    }
    /**
     *根据需求单ID，查找需求单签核
     */
    public List<OrderSign> getOrderSignListByOrderId(Integer orderId){
        return  orderSignMapper.getOrderSignListByOrderId(orderId);
    }
    public void saveAndGetID(OrderSign orderSign) {
        orderSignMapper.saveAndGetID(orderSign);
    }
}
