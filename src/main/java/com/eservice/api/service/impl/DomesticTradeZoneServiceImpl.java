package com.eservice.api.service.impl;

import com.eservice.api.dao.DomesticTradeZoneMapper;
import com.eservice.api.model.domestic_trade_zone.DomesticTradeZone;
import com.eservice.api.service.DomesticTradeZoneService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/07/09.
*/
@Service
@Transactional
public class DomesticTradeZoneServiceImpl extends AbstractService<DomesticTradeZone> implements DomesticTradeZoneService {
    @Resource
    private DomesticTradeZoneMapper domesticTradeZoneMapper;

}
