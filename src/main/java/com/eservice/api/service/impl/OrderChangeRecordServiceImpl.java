package com.eservice.api.service.impl;

import com.eservice.api.dao.OrderChangeRecordMapper;
import com.eservice.api.model.order_change_record.OrderChangeRecord;
import com.eservice.api.model.order_change_record.OrderChangeRecordView;

import com.eservice.api.service.OrderChangeRecordService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/11/14.
 */
@Service
@Transactional
public class OrderChangeRecordServiceImpl extends AbstractService<OrderChangeRecord> implements OrderChangeRecordService {
    @Resource
    private OrderChangeRecordMapper orderChangeRecordMapper;

    public List<OrderChangeRecordView> getChangeRecordList(Integer id, Integer orderId) {
        return orderChangeRecordMapper.getChangeRecordList(id, orderId);
    }

}
