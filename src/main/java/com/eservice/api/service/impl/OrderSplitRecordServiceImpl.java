package com.eservice.api.service.impl;

import com.eservice.api.dao.OrderSplitRecordMapper;
import com.eservice.api.model.order_split_record.OrderSplitRecord;
import com.eservice.api.service.OrderSplitRecordService;
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
public class OrderSplitRecordServiceImpl extends AbstractService<OrderSplitRecord> implements OrderSplitRecordService {
    @Resource
    private OrderSplitRecordMapper orderSplitRecordMapper;

}
