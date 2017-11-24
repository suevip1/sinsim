package com.eservice.api.service.impl;

import com.eservice.api.dao.OrderDetailMapper;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.service.OrderDetailService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/14.
*/
@Service
@Transactional
public class OrderDetailServiceImpl extends AbstractService<OrderDetail> implements OrderDetailService {
    @Resource
    private OrderDetailMapper orderDetailMapper;

}
