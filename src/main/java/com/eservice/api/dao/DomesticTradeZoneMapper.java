package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.domestic_trade_zone.DomesticTradeZone;
import com.eservice.api.model.domestic_trade_zone.DomesticTradeZoneDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DomesticTradeZoneMapper extends Mapper<DomesticTradeZone> {
    List<DomesticTradeZoneDetail> getDomesticTradeZone(@Param("account")String  account, @Param("domesticTradeZone")String domesticTradeZone);
}