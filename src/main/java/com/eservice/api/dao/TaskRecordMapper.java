package com.eservice.api.dao;

import com.eservice.api.core.Mapper;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
import com.eservice.api.model.task_record.TaskRecordExpired;
import com.eservice.api.model.task_record.TaskReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskRecordMapper extends Mapper<TaskRecord> {
    List<TaskRecord> selectTaskReocords(@Param("userAccount") String userAccount);

    List<TaskPlan> selectTaskPlans(@Param("taskRecordId") Integer taskRecordId);

    TaskRecordDetail selectTaskRecordDetail(@Param("taskRecordId") Integer taskRecordId);

    List<TaskRecordDetail> searchTaskRecordDetail(@Param("taskRecordId") Integer taskRecordId,
                                                  @Param("taskName") String taskName,
                                                  @Param("machineOrderNumber") String machineOrderNumber,
                                                  @Param("queryStartTime") String queryStartTime,
                                                  @Param("queryFinishTime") String queryFinishTime,
                                                  @Param("nameplate") String nameplate);

    List<TaskRecordDetail> selectAllTaskRecordDetail();

    List<TaskRecordDetail> selectAllInstallTaskRecordDetailByUserAccount(@Param("userAccount") String userAccount);

    List<TaskRecordDetail> selectAllInstallTaskRecordDetailByUserAccountChuChangJianCe(@Param("userAccount") String userAccount);

    List<TaskRecordDetail> selectAllQaTaskRecordDetailByUserAccount(@Param("userAccount") String userAccount);

    List<TaskRecord> selectNotPlanedTaskRecord(@Param("process_record_id") Integer processRecordId);

    List<TaskRecord> getTaskRecordData(@Param("id") Integer id, @Param("processRecordId") Integer processRecordId);

    List<TaskRecordDetail> selectTaskRecordByMachineNameplate(@Param("namePlate") String namePlate);

    List<TaskRecordDetail> selectTaskRecordByNamePlate(@Param("namePlate") String namePlate);

    List<TaskRecordDetail> selectTaskRecordByNamePlateAndAccount(@Param("namePlate") String namePlate,
                                                                 @Param("account") String account);

    List<TaskRecordDetail> selectUnPlannedTaskRecordByNamePlateAndAccount(@Param("namePlate") String namePlate,
                                                                          @Param("account") String account);

    List<TaskRecordDetail> selectQATaskRecordDetailByAccountAndNamePlate(@Param("namePlate") String namePlate,
                                                                         @Param("account") String account);

    List<TaskRecordDetail> selectUnplannedTaskRecordByAccount(@Param("account") String account);

    int deleteTaskRecordByCondition(@Param("id") Integer id,
                                    @Param("processRecordId") Integer processRecordId,
                                    @Param("nodeKey") Byte nodeKey,
                                    @Param("status") Byte status);

    List<TaskRecordDetail> selectPlanedTaskRecordsByFuzzy(@Param("order_num") String orderNum,
                                                          @Param("machine_strid") String machineStrId,
                                                          @Param("task_name") String taskName,
                                                          @Param("nameplate") String nameplate,
                                                          @Param("install_status") Integer installStatus,
                                                          @Param("machine_type") Integer machineType,
                                                          @Param("query_start_time") String query_start_time,
                                                          @Param("query_finish_time") String query_finish_time);

    List<TaskRecordDetail> selectPlanedTaskRecords(@Param("order_num") String orderNum,
                                                   @Param("machine_strid") String machineStrId,
                                                   @Param("task_name") String taskName,
                                                   @Param("nameplate") String nameplate,
                                                   @Param("install_status") Integer installStatus,
                                                   @Param("machine_type") Integer machineType,
                                                   @Param("query_start_time") String query_start_time,
                                                   @Param("query_finish_time") String query_finish_time);

    List<TaskRecordExpired> getExpiredTaskStatistics(@Param("mode") Integer mode);
    List<TaskReport> selectTaskReports(@Param("task_name") String taskName,
                                       @Param("install_start_time") String installStartTime,
                                       @Param("install_finish_time") String installFinishTime);

    List<TaskRecordDetail> selectMachineOrderStructureTable(@Param("queryMachineOrderCreateTime")String queryMachineOrderCreateTime, //订单录入时间
                                                            @Param("orderNum")String orderNum,
                                                            @Param("sellMan")String sellMan,
                                                            @Param("machineType")Integer machineType,     ///机器类型
                                                            @Param("nameplate")String nameplate,
                                                            @Param("needleNum")String needleNum,
                                                            @Param("headNum") String headNum,
                                                            @Param("electricTrim") String electricTrim,    ///剪线方式
                                                            @Param("machineFinishStatus")String machineFinishStatus, ///
                                                            @Param("taskRecordEndTime")String taskRecordEndTime,///工序完成(结束)时间
                                                            @Param("queryStartTimePlanShipDate")String queryStartTimePlanShipDate,
                                                            @Param("queryFinishTimePlanShipDate")String queryFinishTimePlanShipDate,
                                                            @Param("taskName")String taskName);


}