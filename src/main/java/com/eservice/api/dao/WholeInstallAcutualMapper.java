package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.whole_install_acutual.WholeInstallAcutual;
import com.eservice.api.model.whole_install_acutual.WholeInstallAcutualDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WholeInstallAcutualMapper extends Mapper<WholeInstallAcutual> {
    List<WholeInstallAcutualDetails> selectWholeInstallDetails(@Param("orderNum") String orderNum,@Param("nameplate")String nameplate,@Param("installGroupName")String installGroupName);
}