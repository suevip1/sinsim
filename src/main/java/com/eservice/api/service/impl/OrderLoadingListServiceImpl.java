package com.eservice.api.service.impl;

import com.eservice.api.dao.OrderLoadingListMapper;
import com.eservice.api.model.order_loading_list.OrderLoadingList;
import com.eservice.api.service.OrderLoadingListService;
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
public class OrderLoadingListServiceImpl extends AbstractService<OrderLoadingList> implements OrderLoadingListService {
    @Resource
    private OrderLoadingListMapper orderLoadingListMapper;

}
