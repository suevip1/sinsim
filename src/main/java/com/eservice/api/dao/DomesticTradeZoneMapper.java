package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.domestic_trade_zone.DomesticTradeZone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DomesticTradeZoneMapper extends Mapper<DomesticTradeZone> {
    List<DomesticTradeZone> getDomesticTradeZone(@Param("account")String  account);
}