package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.iot_machine.IotMachine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IotMachineMapper extends Mapper<IotMachine> {

    List<IotMachine> selectIotMachine(@Param("account")String  account,
                                        @Param("nameplate")String  nameplate);
}