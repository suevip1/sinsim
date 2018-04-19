package com.eservice.api.service.impl;

import com.eservice.api.dao.MarketGroupMapper;
import com.eservice.api.model.market_group.MarketGroup;
import com.eservice.api.service.MarketGroupService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2018/04/19.
*/
@Service
@Transactional
public class MarketGroupServiceImpl extends AbstractService<MarketGroup> implements MarketGroupService {
    @Resource
    private MarketGroupMapper marketGroupMapper;

}
