package com.eservice.api.service.impl;

import com.eservice.api.dao.OrderSignMapper;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.service.OrderSignService;
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
public class OrderSignServiceImpl extends AbstractService<OrderSign> implements OrderSignService {
    @Resource
    private OrderSignMapper orderSignMapper;

}
