package com.eservice.api.service.impl;

import com.eservice.api.dao.OrderCancelRecordMapper;
import com.eservice.api.model.order_cancel_record.OrderCancelRecord;
import com.eservice.api.service.OrderCancelRecordService;
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
public class OrderCancelRecordServiceImpl extends AbstractService<OrderCancelRecord> implements OrderCancelRecordService {
    @Resource
    private OrderCancelRecordMapper orderCancelRecordMapper;

}
