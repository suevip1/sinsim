package com.eservice.api.service.impl;

import com.eservice.api.dao.DeviceMapper;
import com.eservice.api.model.device.Device;
import com.eservice.api.service.DeviceService;
import com.eservice.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/24.
*/
@Service
@Transactional
public class DeviceServiceImpl extends AbstractService<Device> implements DeviceService {
    @Resource
    private DeviceMapper deviceMapper;

    public Device findDeviceByMEID(String meid) {
        return deviceMapper.findDeviceByMEID(meid);
    }

}
