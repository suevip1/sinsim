package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.design_dep_info.DesignDepInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DesignDepInfoMapper extends Mapper<DesignDepInfo> {
    List<DesignDepInfo> selectDesignDepInfo(@Param("orderNum") String orderNum,
                                            @Param("saleman") String saleman,
                                            @Param("guestName") String guestName,
                                            @Param("orderStatus") Integer orderStatus,
                                            @Param("drawingLoadingDoneStatus") Integer drawingLoadingDoneStatus,
                                            @Param("machineSpec") String machineSpec,
                                            @Param("keywords") String keywords,
                                            @Param("designer") String designer,
                                            @Param("updateDateStart") String updateDateStart,
                                            @Param("updateDateEnd") String updateDateEnd );

    void saveAndGetID(DesignDepInfo designDepInfo);
}