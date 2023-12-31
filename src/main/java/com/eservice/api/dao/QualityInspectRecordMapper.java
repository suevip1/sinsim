package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecord;
import com.eservice.api.model.quality_inspect_record.QualityInspectRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityInspectRecordMapper extends Mapper<QualityInspectRecord> {
    List<QualityInspectRecordDetail> selectQualityInspectRecordDetail(
            @Param("orderNumber") String orderNumber,
            @Param("taskName") String taskName,
            @Param("recordStatusArr") String[] recordStatusArr,
            @Param("nameplate") String nameplate ,
            @Param("inspectName") String inspectName,
            @Param("inspectType") String inspectType,
            @Param("inspectPhase") String inspectPhase,
            @Param("inspectContent") String inspectContent,
            @Param("inspectPerson") String inspectPerson,
            @Param("recordRemark") String recordRemark,
            @Param("reInspect") String reInspect,
            @Param("queryStartTime") String queryStartTime,
            @Param("queryFinishTime") String queryFinishTime );
			
    List<QualityInspectRecordDetail> selectQualityInspectRecordDetailGroupByMachine(
            @Param("orderNumber") String orderNumber,
            @Param("taskName") String taskName,
            @Param("recordStatus") String recordStatus,
            @Param("nameplate") String nameplate ,
            @Param("inspectName") String inspectName,
            @Param("inspectType") String inspectType,
            @Param("inspectPhase") String inspectPhase,
            @Param("inspectContent") String inspectContent,
            @Param("inspectPerson") String inspectPerson,
            @Param("recordRemark") String recordRemark,
            @Param("reInspect") String reInspect,
            @Param("queryStartTime") String queryStartTime,
            @Param("queryFinishTime") String queryFinishTime );

}