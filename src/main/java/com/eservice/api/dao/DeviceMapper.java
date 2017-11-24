package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.device.Device;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface DeviceMapper extends Mapper<Device> {

    @Select("SELECT * FROM device WHERE meid = #{meid}")
    Device findDeviceByMEID(@Param("meid")String meid);
}