package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.install_plan_actual.InstallPlanActual;
import com.eservice.api.model.install_plan_actual.InstallPlanActualDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InstallPlanActualMapper extends Mapper<InstallPlanActual> {
    List<InstallPlanActualDetails> selectInstallPlanActualDetails(@Param("orderNum") String orderNum,
                                                                  @Param("nameplate") String nameplate,
                                                                  @Param("installGroupName") String installGroupName,
                                                                  @Param("type") String type,
                                                                  @Param("queryStartTime") String queryStartTime,
                                                                  @Param("queryFinishTime") String queryFinishTime,
                                                                  @Param("isNotFinished") Boolean isNotFinished);

    List<InstallPlanActualDetails> selectInstallPlanActualDetails_Parts(@Param("orderNum") String orderNum,
                                                                  @Param("nameplate") String nameplate,
                                                                  @Param("installGroupName") String installGroupName,
                                                                  @Param("type") String type,
                                                                  @Param("queryStartTime") String queryStartTime,
                                                                  @Param("queryFinishTime") String queryFinishTime,
                                                                  @Param("isNotFinished") Boolean isNotFinished);

    InstallPlanActual getInstallPlanActual( @Param("installPlanId") Integer installPlanId);

    List<InstallPlanActual> getInstallPlanActualList( @Param("installPlanId") Integer installPlanId);

    List<InstallPlanActualDetails> selectInstallPlanActualDetailsForShowingBoard(
            @Param("queryStartTime") String queryStartTime,
            @Param("queryFinishTime") String queryFinishTime);

    List<InstallPlanActualDetails> selectInstallPlanActualDetailsForShowingBoard_Parts(
            @Param("queryStartTime") String queryStartTime,
            @Param("queryFinishTime") String queryFinishTime);
}