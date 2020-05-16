package com.eservice.api.web;
import com.eservice.api.model.install_group.InstallGroup;
import com.eservice.api.model.install_plan.InstallPlan;
import com.eservice.api.model.install_plan_actual.InstallPlanActual;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.service.common.NodeDataModel;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.abnormal_image.AbnormalImage;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.quality_record_image.QualityRecordImage;
import com.eservice.api.model.task.Task;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.model.task_quality_record.TaskQualityRecord;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
import com.eservice.api.model.task_record.TaskReport;
import com.eservice.api.service.MachineTypeService;
import com.eservice.api.service.QualityRecordImageService;
import com.eservice.api.service.UserService;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.*;
import com.eservice.api.service.mqtt.MqttMessageHelper;
import com.eservice.api.service.mqtt.ServerToClientMsg;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/12/01.
 */
@RestController
@RequestMapping("/task/record")
public class TaskRecordController {
    @Resource
    private TaskRecordServiceImpl taskRecordService;
    @Resource
    private ProcessRecordServiceImpl processRecordService;
    @Resource
    private CommonService commonService;
    @Resource
    private AbnormalImageServiceImpl abnormalImageService;
    @Resource
    private AbnormalRecordServiceImpl abnormalRecordService;

    /**
     * 导出的生产报表excel表格，和合同excel表格放同个地方
     */
    @Value("${contract_excel_output_dir}")
    private String taskRecordExcelOutputDir;

    @Value("${abnormal_images_saved_dir}")
    private String imagesSavedDir;
    @Value("${quality_images_saved_dir}")
    private String qualityImagesSavedDir;
    /**
     * 导出计划的excel表格，和合同excel表格放同个地方
     */
    @Value("${contract_excel_output_dir}")
    private String abnoramlExcelOutputDir;
    @Resource
    private UserService userService;
    @Resource
    private MachineServiceImpl machineService;
    @Resource
    private MachineTypeService machineTypeService;
    @Resource
    private TaskServiceImpl taskService;
    @Resource
    private MachineOrderServiceImpl machineOrderService;
    @Resource
    private MqttMessageHelper mqttMessageHelper;
    @Resource
    private TaskQualityRecordServiceImpl taskQualityRecordService;
    @Resource
    private QualityRecordImageService qualityRecordImageService;
    @Resource
    private InstallGroupServiceImpl installGroupService;
    @Resource
    private InstallPlanServiceImpl installPlanService;
    @Resource
    private InstallPlanActualServiceImpl installPlanActualService;

    @PostMapping("/add")
    public Result add(String taskRecord) {
        TaskRecord taskRecord1 = JSON.parseObject(taskRecord, TaskRecord.class);
        taskRecordService.save(taskRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        taskRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String taskRecord) {
        TaskRecord taskRecord1 = JSON.parseObject(taskRecord, TaskRecord.class);
        taskRecordService.update(taskRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        TaskRecord taskRecord = taskRecordService.findById(id);
        return ResultGenerator.genSuccessResult(taskRecord);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据userAccount 返回该用户的 Tasks
     *
     * @param page
     * @param size
     * @param userAccount
     * @return 返回该用户的 Tasks
     */
    @PostMapping("/selectTaskReocords")
    public Result selectTaskReocords(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                     @RequestParam String userAccount) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.selectTaskReocords(userAccount);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据 taskRecord.id 返回 task_plans
     *
     * @param page
     * @param size
     * @param taskRecordId
     * @return
     */
    @PostMapping("/selectTaskPlans")
    public Result selectTaskPlans(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                  @RequestParam Integer taskRecordId) {
        PageHelper.startPage(page, size);
        List<TaskPlan> list = taskRecordService.selectTaskPlans(taskRecordId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据  taskRecord.id 返回processRecord, machine，machine order,order_loading_list 信息。
     *
     * @param taskRecordId
     * @return
     */
    @PostMapping("selectTaskRecordDetail")
    public Result selectTaskRecordDetail(@RequestParam Integer taskRecordId) {
        TaskRecordDetail taskRecordDetail = taskRecordService.selectTaskRecordDetail(taskRecordId);
        return ResultGenerator.genSuccessResult(taskRecordDetail);
    }

    /**
     * 根据条件返回 安装开始时间，结束时间，安装组长，机器铭牌号，订单号，安装任务名称，工序的标准耗时。
     * （和 selectTaskRecordDetail 类似，为避免前端app修改，后端另外新建了这个接口）
     */
    @PostMapping("searchTaskRecordDetail")
    public Result searchTaskRecordDetail(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                         Integer taskRecordId,
                                         String taskName,
                                         String machineOrderNumber,
                                         String queryStartTime,
                                         String queryFinishTime,
                                         String nameplate) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail>  taskRecordDetailList = taskRecordService.searchTaskRecordDetail(taskRecordId,taskName,machineOrderNumber,queryStartTime,queryFinishTime,nameplate);
        PageInfo pageInfo = new PageInfo(taskRecordDetailList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 给生产部管理员返回所有detail，其中不限于包括：
     * {
     * "machine_id":"",
     * "task_name":"",
     * "status":"",
     * "交货日期":"",
     * "计划日期":"",
     * }
     *
     * @param page
     * @param size
     * @return
     */
    @PostMapping("selectAllTaskRecordDetail")
    public Result selectAllTaskRecordDetail(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> ListTaskRecordDetail = taskRecordService.selectAllTaskRecordDetail();
        PageInfo pageInfo = new PageInfo(ListTaskRecordDetail);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据用户返回所有安装组detail，（除去status为初始化、已计划和质检完成的task_record) ，其中不限于包括：
     * "machine_id":"", 	-->machine.machine_id
     * "task_name":"",	-->task_record.task_name
     * "status":"",		-->task_record.status
     * "交货日期":"",		-->machine_order.contract_ship_date
     * "计划日期":"",		-->machine_order.plan_ship_date
     *
     *特别地，包含了跳过的工序的机器，在最后的“出厂检测”工序，不要显示出来，因为这些机器还不应该被出厂检测。
     * @param userAccount
     * @return
     */
    @PostMapping("selectAllInstallTaskRecordDetailByUserAccount")
    public Result selectAllInstallTaskRecordDetailByUserAccount(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                                                @RequestParam String userAccount) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> ListTaskRecordDetail = taskRecordService.selectAllInstallTaskRecordDetailByUserAccount(userAccount);
        PageInfo pageInfo = new PageInfo(ListTaskRecordDetail);
            return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据用户返回所有检测员detail，其中不限于包括：
     * "machine_id":"", 	-->machine.machine_id
     * "task_name":"",	-->task_record.task_name
     * "status":"",		-->task_record.status
     * "交货日期":"",		-->machine_order.contract_ship_date
     * "计划日期":"",		-->machine_order.plan_ship_date
     *
     * @param page
     * @param size
     * @param userAccount
     * @return
     */
    @PostMapping("selectAllQaTaskRecordDetailByUserAccount")
    public Result selectAllQaTaskRecordDetailByUserAccount(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                                           @RequestParam String userAccount) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> ListTaskRecordDetail = taskRecordService.selectAllQaTaskRecordDetailByUserAccount(userAccount);
        PageInfo pageInfo = new PageInfo(ListTaskRecordDetail);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //根据机器的流程记录ID，返回未计划的作业任务（task plan）
    @PostMapping("/selectNotPlanedTaskRecord")
    public Result selectNotPlanedTaskRecord(@RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "0") Integer size,
                                            Integer processRecordID) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.selectNotPlanedTaskRecord(processRecordID);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 获取已计划的task record
     */
    @PostMapping("/selectPlanedTaskRecords")
    public Result selectPlanedTaskRecords(@RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "0") Integer size,
                                          String orderNum,
                                          String machineStrId,
                                          String taskName,
                                          String nameplate,
                                          Integer installStatus,
                                          Integer machineType,
                                          String query_start_time,
                                          String query_finish_time,
                                          @RequestParam(defaultValue = "true") Boolean is_fuzzy) {

        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectPlanedTaskRecords(orderNum, machineStrId, taskName, nameplate, installStatus, machineType, query_start_time, query_finish_time, is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 导出计划到excel表格
     * @param orderNum
     * @param machineStrId
     * @param taskName
     * @param nameplate
     * @param installStatus
     * @param machineType
     * @param query_start_time
     * @param query_finish_time
     * @param is_fuzzy
     * @return
     */
    @PostMapping("/export")
    public Result export(                 String orderNum,
                                          String machineStrId,
                                          String taskName,
                                          String nameplate,
                                          Integer installStatus,
                                          Integer machineType,
                                          String query_start_time,
                                          String query_finish_time,
                                          @RequestParam(defaultValue = "true") Boolean is_fuzzy) {

        List<TaskRecordDetail> list = taskRecordService.selectPlanedTaskRecords(orderNum, machineStrId, taskName, nameplate, installStatus, machineType, query_start_time, query_finish_time, is_fuzzy);

        HSSFWorkbook wb = null;
        FileOutputStream out = null;
        String downloadPath = "";
        /*
        返回给docker外部下载
         */
        String downloadPathForNginx = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateString;
        try {
            //生成一个空的Excel文件
            wb=new HSSFWorkbook();
            Sheet sheet1=wb.createSheet("sheet1");

            //设置标题行格式
            HSSFCellStyle headcellstyle = wb.createCellStyle();
            HSSFFont headfont = wb.createFont();
            headfont.setFontHeightInPoints((short) 10);
            headfont.setBold(true);//粗体显示
            headcellstyle.setFont(headfont);
            Row row;
            //创建行和列
            for(int r=0;r<list.size() + 1; r++ ) {
                row = sheet1.createRow(r);//新创建一行，行号为row+1
                //序号，需求单号，机器编号，机型，工序，状态，安装的开始时间，安装的结束时间，质检的开始时间，质检的结束时间
                //计划完成时间，合同交货日期，计划交货日期
                for(int c=0; c< 13; c++){
                    row.createCell(c);//创建一个单元格，列号为col+1
                    sheet1.getRow(0).getCell(c).setCellStyle(headcellstyle);
                }
            }
            for(int k=1; k<13; k++){
                sheet1.setColumnWidth(k,4500);
            }

            //第一行为标题
            sheet1.getRow(0).getCell(0).setCellValue("序号");
            sheet1.getRow(0).getCell(1).setCellValue("需求单号");
            sheet1.getRow(0).getCell(2).setCellValue("机器编号");
            sheet1.getRow(0).getCell(3).setCellValue("机型");
            sheet1.getRow(0).getCell(4).setCellValue("工序");
            sheet1.getRow(0).getCell(5).setCellValue("状态");
            sheet1.getRow(0).getCell(6).setCellValue("安装的开始时间");
            sheet1.getRow(0).getCell(7).setCellValue("安装的结束时间");
            sheet1.getRow(0).getCell(8).setCellValue("质检的开始时间");
            sheet1.getRow(0).getCell(9).setCellValue("质检的结束时间");
            sheet1.getRow(0).getCell(10).setCellValue("计划完成时间");
            sheet1.getRow(0).getCell(11).setCellValue("合同交货日期");
            sheet1.getRow(0).getCell(12).setCellValue("计划交货日期");

            //第二行开始，填入值
            Machine machine = null;
            MachineOrder machineOrder = null;
            MachineType machineType1 = null;
            Byte taskStatus = 0;
            for(int r=0; r<list.size(); r++ ) {
                row = sheet1.getRow(r + 1);
                row.getCell(0).setCellValue(r + 1);

                //需求单号
                if(list.get(r).getMachineOrder().getOrderNum() != null ) {
                    row.getCell(1).setCellValue(list.get(r).getMachineOrder().getOrderNum());
                }
                //机器编号
                if(list.get(r).getMachine().getNameplate() != null ) {
                    row.getCell(2).setCellValue(list.get(r).getMachine().getNameplate());
                }
                int machineTypeID = list.get(r).getMachine().getMachineType();


                machine = machineService.selectMachinesByNameplate(list.get(r).getMachine().getNameplate());

                /**
                 * 获取机型类型的名称 machine_type.name
                 */
                machineType1 = machineTypeService.findById(machineTypeID);
                if(machineType1 != null) {
                    row.getCell(3).setCellValue(machineType1.getName()); //机型
                }
                // 工序
                if(list.get(r).getTaskName() != null ) {
                    row.getCell(4).setCellValue(list.get(r).getTaskName());
                }
                //状态
                taskStatus = list.get(r).getStatus();
                if(taskStatus == Constant.TASK_INITIAL) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_INITIAL);
                } else if(taskStatus == Constant.TASK_PLANED) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_PLANED);
                } else if(taskStatus == Constant.TASK_INSTALL_WAITING) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_INSTALL_WAITING);
                } else if(taskStatus == Constant.TASK_INSTALLING) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_INSTALLING);
                } else if(taskStatus == Constant.TASK_INSTALLED) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_INSTALLED);
                } else if(taskStatus == Constant.TASK_QUALITY_DOING) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_QUALITY_DOING);
                } else if(taskStatus == Constant.TASK_QUALITY_DONE) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_QUALITY_DONE);
                } else if(taskStatus == Constant.TASK_INSTALL_ABNORMAL) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_INSTALL_ABNORMAL);
                } else if(taskStatus == Constant.TASK_QUALITY_ABNORMAL) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_QUALITY_ABNORMAL);
                } else if(taskStatus == Constant.TASK_SKIP) {
                    row.getCell(5).setCellValue(Constant.STR_TASK_SKIP);
                }

                // 安装的开始时间
                if( list.get(r).getInstallBeginTime() != null) {
                    dateString = formatter2.format(list.get(r).getInstallBeginTime());
                    row.getCell(6).setCellValue(dateString);
                }
                // 安装的结束时间
                if( list.get(r).getInstallEndTime() != null) {
                    dateString = formatter2.format(list.get(r).getInstallEndTime());
                    row.getCell(7).setCellValue(dateString);
                }
                // 质检的开始时间
                if( list.get(r).getQualityBeginTime()  != null) {
                    dateString = formatter2.format(list.get(r).getQualityBeginTime());
                    row.getCell(8).setCellValue(dateString);
                }
                // 质检的结束时间
                if( list.get(r).getQualityEndTime() != null) {
                    dateString = formatter2.format(list.get(r).getQualityEndTime());
                    row.getCell(9).setCellValue(dateString);
                }
                //计划完成时间
                if( list.get(r).getTaskPlan().getPlanTime() != null) {
                    dateString = formatter.format(list.get(r).getTaskPlan().getPlanTime());
                    row.getCell(10).setCellValue(dateString);
                }
                //合同交货日期
                if( list.get(r).getMachineOrder().getContractShipDate()!= null) {
                    dateString = formatter.format(list.get(r).getMachineOrder().getContractShipDate());
                    row.getCell(11).setCellValue(dateString);
                }
                //计划交货日期
                if( list.get(r).getMachineOrder().getPlanShipDate()!= null) {
                    dateString = formatter.format(list.get(r).getMachineOrder().getPlanShipDate());
                    row.getCell(12).setCellValue(dateString);
                }
            }
            downloadPath = abnoramlExcelOutputDir + "导出计划" + ".xls";
            downloadPathForNginx = "/excel/" + "导出计划" + ".xls";
            out = new FileOutputStream(downloadPath);
            wb.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if("".equals(downloadPath)) {
            return ResultGenerator.genFailResult("异常导出失败!");
        }else {
            return ResultGenerator.genSuccessResult(downloadPathForNginx);
        }

    }

    /**
     *  在”生产管理”的“生产报表”导出到excel.
     */
    @PostMapping("/exportToExcel")
    public Result exportToExcel(            Integer taskRecordId,
                                            String taskName,
                                            String machineOrderNumber,
                                            String queryStartTime,
                                            String queryFinishTime,
                                            String nameplate) {

        List<TaskRecordDetail> list = taskRecordService.searchTaskRecordDetail(taskRecordId,taskName,machineOrderNumber,queryStartTime,queryFinishTime,nameplate);

        HSSFWorkbook wb = null;
        FileOutputStream out = null;
        String downloadPath = "";
        /*
        返回给docker外部下载
         */
        String downloadPathForNginx = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateString;
        try {
            //生成一个空的Excel文件
            wb=new HSSFWorkbook();
            Sheet sheet1=wb.createSheet("生产报表");

            //设置标题行格式
            HSSFCellStyle headcellstyle = wb.createCellStyle();
            HSSFFont headfont = wb.createFont();
            headfont.setFontHeightInPoints((short) 10);
            headfont.setBold(true);//粗体显示
            headcellstyle.setFont(headfont);
            Row row;
            //创建行和列
            for(int r=0;r<list.size() + 1; r++ ) {
                row = sheet1.createRow(r);//新创建一行，行号为row+1
                //序号，工序名称，订单号，机器编号，安装组长，安装的开始时间，安装的结束时间，耗时
                for(int c=0; c<8; c++){
                    row.createCell(c);//创建一个单元格，列号为col+1
                    sheet1.getRow(0).getCell(c).setCellStyle(headcellstyle);
                }
            }
            for(int k=1; k<8; k++){
                sheet1.setColumnWidth(k,4500);
            }

            //第一行为标题
            sheet1.getRow(0).getCell(0).setCellValue("序号");
            sheet1.getRow(0).getCell(1).setCellValue("工序名称");
            sheet1.getRow(0).getCell(2).setCellValue("订单号");
            sheet1.getRow(0).getCell(3).setCellValue("铭牌号/机器类型/针数/头数/头距/X行程/Y行程");
            sheet1.getRow(0).getCell(4).setCellValue("安装组长");
            sheet1.getRow(0).getCell(5).setCellValue("开始时间");
            sheet1.getRow(0).getCell(6).setCellValue("结束时间");
            sheet1.getRow(0).getCell(7).setCellValue("耗时(分钟)");

            //第二行开始，填入值
            MachineType machineType1 = null;
            Byte taskStatus = 0;
            for(int r=0; r<list.size(); r++ ) {
                row = sheet1.getRow(r + 1);
                row.getCell(0).setCellValue(r + 1);

                //工序名称
                if(list.get(r).getTaskName() != null ) {
                    row.getCell(1).setCellValue(list.get(r).getTaskName());
                }
                //订单号
                if(list.get(r).getMachineOrder().getOrderNum() != null ) {
                    row.getCell(2).setCellValue(list.get(r).getMachineOrder().getOrderNum());
                }
                //机器编号等机器信息
                Machine machine = list.get(r).getMachine();
                MachineOrder machineOrder = list.get(r).getMachineOrder();
                MachineOrderDetail machineOrderDetail = machineOrderService.getOrderAllDetail(machineOrder.getId());
                if(machine !=null && machineOrder != null){
                    String machineInfo = machine.getNameplate() + "/"
                            + machineOrderDetail.getMachineType().getName() + "/"
                            + machineOrder.getNeedleNum() + "/"
                            + machineOrder.getHeadNum() + "/"
                            + machineOrder.getHeadDistance() + "/"
                            + machineOrder.getxDistance() + "/"
                            + machineOrder.getyDistance();
                    row.getCell(3).setCellValue(machineInfo);
                }
                //安装组长
                if(list.get(r).getLeader() != null ) {
                    row.getCell(4).setCellValue(list.get(r).getLeader());
                }
                //开始时间
                if(list.get(r).getInstallBeginTime() != null ) {
                    dateString = formatter.format(list.get(r).getInstallBeginTime());
                    row.getCell(5).setCellValue(dateString);
                }
                //结束时间
                if(list.get(r).getInstallEndTime() != null ) {
                    dateString = formatter.format(list.get(r).getInstallEndTime());
                    row.getCell(6).setCellValue(dateString);
                }
                //耗时
                if(list.get(r).getInstallBeginTime() != null && list.get(r).getInstallEndTime() != null ) {
                    long minHourDay = commonService.secondsToMin( list.get(r).getInstallEndTime().getTime() - list.get(r).getInstallBeginTime().getTime());
                    row.getCell(7).setCellValue(minHourDay);
                }

            }
            downloadPath = taskRecordExcelOutputDir + "生产报表" + ".xls";
            downloadPathForNginx = "/excel/" + "生产报表" + ".xls";
            out = new FileOutputStream(downloadPath);
            wb.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if("".equals(downloadPath)) {
            return ResultGenerator.genFailResult("异常导出失败!");
        }else {
            return ResultGenerator.genSuccessResult(downloadPathForNginx);
        }

    }
    @PostMapping("/getTaskRecordData")
    public Result getTaskRecordData(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") Integer id,
            @RequestParam(defaultValue = "0") Integer processRecordId
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecord> list = taskRecordService.getTaskRecordData(id, processRecordId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/updateStatus")
    @Transactional(rollbackFor = Exception.class)
    public Result updateStatus(String taskRecord, String processRecord) {
        TaskRecord tr = JSON.parseObject(taskRecord, TaskRecord.class);
        Integer id = tr.getId();
        if (id == null || id < 0) {
            return ResultGenerator.genFailResult("TaskRecord的ID为空，数据更新失败！");
        }
        taskRecordService.update(tr);

        ProcessRecord pr = JSON.parseObject(processRecord, ProcessRecord.class);
        id = pr.getId();
        if (id == null || id < 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultGenerator.genFailResult("ProcessRecord的ID为空，数据更新失败！");
        }
        processRecordService.update(pr);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 比如app扫码&结束开始时调用该接口，开始/完成一个工序各调用一次。
     * （质检报正常也是调该接口，即无论是否质检都调这个接口）
     *
     * app扫码报异常时，调用 addTrArAi （app报正常时，调用的是 updateTaskInfo， 少了两个参数）
     * （质检报正常也是调addTrArAi）
     * @param taskRecord
     * @return
     */
    @PostMapping("/updateTaskInfo")
    @Transactional(rollbackFor = Exception.class)
    public Result updateTaskInfo(String taskRecord) {
        TaskRecord tr = JSON.parseObject(taskRecord, TaskRecord.class);
        Integer id = tr.getId();
        if (id == null || id < 0) {
            return ResultGenerator.genFailResult("TaskRecord的ID为空，数据更新失败！");
        }
        //防止开始安装时间为空的问题
        if(tr.getStatus().intValue() == Constant.TASK_INSTALLING.intValue()) {
            try{
                if( tr.getUpdateTime()!=null)
                {
                    Integer timespan=(int)((new Date().getTime() - tr.getUpdateTime().getTime()) / (1000 * 60 * 60));
                    tr.setWaitTimespan(timespan);
                    //同步更新到ProcessRecord对应的task中
                    ProcessRecord  processRecord = processRecordService.findById(tr.getProcessRecordId());
                    String nodeData = processRecord.getNodeData();
                    List<NodeDataModel> ndList = JSON.parseArray(nodeData, NodeDataModel.class);
                    for(int i=0;i<ndList.size();i++)
                    {
                        if(ndList.get(i).getKey().equals(String.valueOf(tr.getNodeKey())))
                        {
                            ndList.get(i).setWaitTimespan(timespan);
                            ndList.get(i).setBeginTime(new Date().toString());
                            break;
                        }
                    }
                    processRecord.setNodeData(JSON.toJSONString(ndList));
                    processRecordService.update(processRecord);
                }
            }
            catch (Exception ex) {
            }
            tr.setInstallBeginTime(new Date());
        }
        else if(tr.getStatus().intValue() == Constant.TASK_PLANED.intValue()
        ||tr.getStatus().intValue() == Constant.TASK_INSTALL_WAITING.intValue()) {
            ProcessRecord  pr = processRecordService.findById(tr.getProcessRecordId());
            String nData = pr.getNodeData();
            List<NodeDataModel> ndList = JSON.parseArray(nData, NodeDataModel.class);
                    for(int i=0;i<ndList.size();i++)
                    {
                        if(ndList.get(i).getKey().equals(String.valueOf(tr.getNodeKey())))
                        {
                            if(ndList.get(i).getBeginTime()==""||ndList.get(i).getBeginTime()==null)
                            {
                                ndList.get(i).setBeginTime(new Date().toString());
                            }
                            break;
                        }
                    }
                    pr.setNodeData(JSON.toJSONString(ndList));
                    processRecordService.update(pr);
        }
        taskRecordService.update(tr);

        Integer prId = tr.getProcessRecordId();
        if (prId == null || prId < 0) {
            Logger.getLogger("").log(Level.INFO, "processrecord Id 为空");
        } else {
            //Update task record相关的状态
            if(!commonService.updateTaskRecordRelatedStatus(tr)) {
                //更新出错进行事务回退
                throw new RuntimeException();
            }
        }
        //找到工序对应的quality_user_id
        String taskName = tr.getTaskName();
        Condition condition = new Condition(Task.class);
        condition.createCriteria().andCondition("task_name = ", taskName);
        List<Task> taskList = taskService.findByCondition(condition);
        if(taskList == null || taskList.size() <= 0) {
            throw new RuntimeException();
        }

        ProcessRecord pr = processRecordService.findById(prId);
        Machine machine = machineService.findById(pr.getMachineId());
        MachineOrder machineOrder = machineOrderService.findById(machine.getOrderId());
        ServerToClientMsg msg = new ServerToClientMsg();
        msg.setOrderNum(machineOrder.getOrderNum());
        msg.setNameplate(machine.getNameplate());
        if(tr.getStatus().equals(Constant.TASK_INSTALLED)) {
            //MQTT 如果当前工序状态是安装完成等待质检的状态，则通知App
            mqttMessageHelper.sendToClient(Constant.S2C_TASK_QUALITY + taskList.get(0).getQualityUserId(), JSON.toJSONString(msg));
        }

        //开始安装时不用，只有在安装结束时才要更新 --> 现在没有质检了，扫码完成时，app直接发 质检完成
        if(tr.getStatus().equals(Constant.TASK_QUALITY_DONE)) {
            createInstallPlanActual(tr);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据已完成的工序，自动生成 【对应工序的】的总装排产的实际完成情况
     */
    public void createInstallPlanActual(TaskRecord tr){
        /**
         * 如果机器某个工序已经完成(结束扫码)，
         * 则该机器的 对应的总装的 工序的针数头数自动填写为全部完成，
         * 这样app用户就不需要重复去报告总装工序的情况。
         *
         * task_record --> process_record --> machine --> install_plan --> install_plan_actual
         */
        ProcessRecord pr = processRecordService.findById(tr.getProcessRecordId());
        Machine machine = machineService.findById(pr.getMachineId());
        List<InstallPlan> installPlanList = installPlanService.getInstallPlanByMachineId(machine.getId());

        MachineOrder machineOrder = machineOrderService.findById(machine.getOrderId());
        InstallGroup installGroup = installGroupService.getInstallGroupByTaskName(tr.getTaskName());
        for (InstallPlan installplan: installPlanList) {
            if(installGroup.getId() == installplan.getInstallGroupId()) {
                InstallPlanActual installPlanActual = new InstallPlanActual();
                installPlanActual.setCreateDate(new Date());
                installPlanActual.setInstallPlanId(installplan.getId());
                installPlanActual.setHeadCountDone(commonService.getRealSumValue(machineOrder.getHeadNum()));
                installPlanActualService.save(installPlanActual);

                Logger.getLogger("").log(Level.INFO, "自动生成 实际总装 " + installPlanActual.getId() + ", 头数 " + installPlanActual.getHeadCountDone());///这里可以获取id ! ...
                break;
            } else {
//                Logger.getLogger("").log(Level.INFO, installGroup.getGroupName() + ", 无需生成 ");
            }
        }
    }

    /**
     * 根据机器铭牌（即机器编号）查询对应的机器正在操作的taskRecordDetail(全部状态) 。
     *
     * @param page
     * @param size
     * @param namePlate
     * @return
     */
    //TODO: 该接口返回的数据似乎不完整，待查。
    @PostMapping("/selectTaskRecordByMachineNameplate")
    public Result selectTaskRecordByMachineNameplate(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String namePlate
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectTaskRecordByMachineNameplate(namePlate);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据机器的铭牌号（nameplate）查询对应的机器正在操作的taskRecordDetail(全部状态)。
     *
     * @param page
     * @param size
     * @param namePlate
     * @return
     */
    @PostMapping("/selectTaskRecordByNamePlate")
    public Result selectTaskRecordByNamePlate(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String namePlate
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectTaskRecordByNamePlate(namePlate);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据account和机器的铭牌号（nameplate），
     * 返回对应机器正在操作的步骤（除去status为初始化、已计划和质检完成的task_record），且属于该account的排班计划。
     *
     * @param page
     * @param size
     * @param namePlate
     * @param account
     * @return
     */
    @PostMapping("/selectTaskRecordByNamePlateAndAccount")
    public Result selectTaskRecordByNamePlateAndAccount(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String namePlate,
            @RequestParam(defaultValue = "0") String account
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectTaskRecordByNamePlateAndAccount(namePlate, account);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 返回待计划的Task_record具体信息
     *
     * @param page
     * @param size
     * @param namePlate
     * @param account
     * @return
     */
    @PostMapping("/selectUnPlannedTaskRecordByNamePlateAndAccount")
    public Result selectUnPlannedTaskRecordByNamePlateAndAccount(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String namePlate,
            @RequestParam(defaultValue = "0") String account
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectUnPlannedTaskRecordByNamePlateAndAccount(namePlate, account);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据account返回该用户的的待计划安装任务
     *
     * @param page
     * @param size
     * @param account
     * @return
     */
    @PostMapping("/selectUnplannedTaskRecordByAccount")
    public Result selectUnplandTaskRecordByAccount(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String account
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectUnplannedTaskRecordByAccount(account);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 返回满足user+nameplate 且处于安装完成待质检和质检异常状态的质检任务
     *
     * @param page
     * @param size
     * @param namePlate
     * @param account
     * @return
     */
    @PostMapping("selectQATaskRecordDetailByAccountAndNamePlate")
    public Result selectQATaskRecordDetailByAccountAndNamePlate(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam(defaultValue = "0") String namePlate,
            @RequestParam(defaultValue = "0") String account
    ) {
        PageHelper.startPage(page, size);
        List<TaskRecordDetail> list = taskRecordService.selectQATaskRecordDetailByAccountAndNamePlate(namePlate, account);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * task_record, abnormal_record, abnormal_image  3个表一起更新。
     * app端，在安装异常时需要更新task_record(update)，增加 abnormal_record(add), abnormal_image (add)
     *
     * app扫码报异常时，调用addTrArAi （app报正常时，调用的是 updateTaskInfo， 少了异常记录和文件两个参数）
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addTrArAi")
    public Result addTrArAi(@RequestParam String taskRecord,
                            @RequestParam String abnormalRecord,
                            @RequestParam MultipartFile[] files) {

        //task_record(update)
        TaskRecord taskRecord1 = JSON.parseObject(taskRecord, TaskRecord.class);
        taskRecordService.update(taskRecord1);

        //abnormal_record add:
        AbnormalRecord abnormalRecord1 = JSON.parseObject(abnormalRecord, AbnormalRecord.class);
        //在安装异常时，并没有SolutionUser，即还不知道SolutionUser，所以这里设为null
        abnormalRecord1.setSolutionUser(null);
        abnormalRecordService.saveAndGetID(abnormalRecord1);
        //获取保存后分配到的id
        Integer abnormalRecordId = abnormalRecord1.getId();

        /**
         * abnormal_image add
         * 因为此时从app端无法知道 abnormal_record_id，所以需要在服务端获取abnormal_record_id
         * abnormal_image的create_time和abnormal_record.create_time一样
         */
        AbnormalImage abnormalImage1 = new AbnormalImage();
        File dir = new File(imagesSavedDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        String machineID = machineService.searchMachineByAbnormalRecordId(abnormalRecordId).getMachineStrId();
        Integer orderId = machineService.searchMachineByAbnormalRecordId(abnormalRecordId).getOrderId();
        String orderNum = machineOrderService.findById(orderId).getOrderNum();
        List<String> listResultPath = new ArrayList<>() ;
        for(int i=0; i<files.length; i++) {
            try {
                listResultPath.add( commonService.saveFile(imagesSavedDir, files[i], machineID, orderNum, Constant.ABNORMAL_IMAGE, i ));
            } catch (IOException e) {
                e.printStackTrace();
                //抛异常引发回滚，防止数据只更新了前面部分。
                throw new RuntimeException();
            }
        }
        if (listResultPath.size() != files.length){
            throw new RuntimeException();
        } else {
            abnormalImage1.setImage(listResultPath.toString());
            abnormalImage1.setAbnormalRecordId(abnormalRecordId);
            abnormalImage1.setCreateTime(abnormalRecord1.getCreateTime());
            abnormalImageService.save(abnormalImage1);
        }

        //找到工序对应的quality_user_id
        String taskName = taskRecord1.getTaskName();
        Condition condition = new Condition(Task.class);
        condition.createCriteria().andCondition("task_name = ", taskName);
        List<Task> taskList = taskService.findByCondition(condition);
        if(taskList == null || taskList.size() <= 0) {
            throw new RuntimeException();
        }

        Integer prId = taskRecord1.getProcessRecordId();
        if (prId == null || prId < 0) {
            Logger.getLogger("").log(Level.INFO, "processrecord Id 为空");
        } else {
            //Update task record相关的状态
            if(!commonService.updateTaskRecordRelatedStatus(taskRecord1)) {
                //更新出错进行事务回退
                throw new RuntimeException();
            }
        }

        ProcessRecord pr = processRecordService.findById(prId);
        Machine machine = machineService.findById(pr.getMachineId());
        MachineOrder machineOrder = machineOrderService.findById(machine.getOrderId());
        ServerToClientMsg msg = new ServerToClientMsg();
        msg.setOrderNum(machineOrder.getOrderNum());
        msg.setNameplate(machine.getNameplate());
        if(taskRecord1.getStatus().equals(Constant.TASK_INSTALL_ABNORMAL)) {
            //MQTT 发生安装异常时，通知生产部管理员
            //mqttMessageHelper.sendToClient(Constant.S2C_INSTALL_ABNORMAL + taskList.get(0).getGroupId(), JSON.toJSONString(msg));
            //MQTT 发生安装异常时，通知对应质检员
            mqttMessageHelper.sendToClient(Constant.S2C_INSTALL_ABNORMAL_TO_QUALITY + taskList.get(0).getQualityUserId(), JSON.toJSONString(msg));
        }
        //报异常，不应该生成对应 InstallPlanActual
        //createInstallPlanActual(taskRecord1);
        return ResultGenerator.genSuccessResult("3个表 task_record + abnormal_record + abnormal_image更新成功");
    }

    /**
     * 3个表 task_record/TaskQualityRecord/QualityRecordImage 更新。
     * app端，上传质检异常状态，task_record(update)，增加 TaskQualityRecord(add), QualityRecordImage (add)
     * 没有质检员了，这个接口没在用。
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addTrTqrQri")
    public Result addTrTqrQri(@RequestParam String taskRecord,
                            @RequestParam String taskQualityRecord,
                            @RequestParam MultipartFile[] files) {
        //task_record(update)
        TaskRecord taskRecord1 = JSON.parseObject(taskRecord, TaskRecord.class);
        taskRecordService.update(taskRecord1);

        TaskQualityRecord taskQualityRecord1 = JSON.parseObject(taskQualityRecord, TaskQualityRecord.class);
        //质检异常类型，目前未使用，default值为1
        taskQualityRecord1.setAbnormalType(1);
        taskQualityRecordService.saveAndGetID(taskQualityRecord1);
        //获取保存后分配到的id
        Integer taskQualityRecordId = taskQualityRecord1.getId();

        //构建 qualityRecordImage1
        QualityRecordImage qualityRecordImage1 = new QualityRecordImage();
        File dir = new File(qualityImagesSavedDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        String machineID = machineService.searchMachineByTaskQualityRecordId(taskQualityRecordId).getMachineStrId();
        Integer orderId = machineService.searchMachineByTaskQualityRecordId(taskQualityRecordId).getOrderId() ;
        String orderNum = machineOrderService.findById(orderId).getOrderNum();
        List<String> listResultPath = new ArrayList<>() ;
        for(int i=0; i<files.length; i++) {
            try {
                listResultPath.add( commonService.saveFile(qualityImagesSavedDir, files[i], machineID, orderNum, Constant.QUALITY_IMAGE, i));
            } catch (IOException e) {
                e.printStackTrace();
                //抛异常引发回滚，防止数据只更新了前面部分。
                throw new RuntimeException();
            }
        }
        if (listResultPath.size() != files.length){
            throw new RuntimeException();
        } else {
            qualityRecordImage1.setTaskQualityRecordId(taskQualityRecordId);
            qualityRecordImage1.setImage(listResultPath.toString());
            qualityRecordImage1.setCreateTime(taskQualityRecord1.getCreateTime());
            qualityRecordImageService.save(qualityRecordImage1);
        }

        //找到工序对应的quality_user_id
        String taskName = taskRecord1.getTaskName();
        Condition condition = new Condition(Task.class);
        condition.createCriteria().andCondition("task_name = ", taskName);
        List<Task> taskList = taskService.findByCondition(condition);
        if(taskList == null || taskList.size() <= 0) {
            throw new RuntimeException();
        }

        Integer prId = taskRecord1.getProcessRecordId();
        if (prId == null || prId < 0) {
            Logger.getLogger("").log(Level.INFO, "processrecord Id 为空");
        } else {
            //Update task record相关的状态
            if(!commonService.updateTaskRecordRelatedStatus(taskRecord1)) {
                //更新出错进行事务回退
                throw new RuntimeException();
            }
        }

        ProcessRecord pr = processRecordService.findById(prId);
        Machine machine = machineService.findById(pr.getMachineId());
        MachineOrder machineOrder = machineOrderService.findById(machine.getOrderId());
        ServerToClientMsg msg = new ServerToClientMsg();
        msg.setOrderNum(machineOrder.getOrderNum());
        msg.setNameplate(machine.getNameplate());
       if(taskRecord1.getStatus().equals(Constant.TASK_QUALITY_ABNORMAL)) {
           //MQTT 发生质检异常时，通知安装组长以及生产部管理员
           mqttMessageHelper.sendToClient(Constant.S2C_QUALITY_ABNORMAL + taskList.get(0).getGroupId(), JSON.toJSONString(msg));
        }
        return ResultGenerator.genSuccessResult("3个表 task_record + TaskQualityRecord + QualityRecordImage 更新成功");
    }

    /**
     * 返回时间段内某安装工序taskName的安装记录
     *
     * @param page
     * @param size
     * @param taskName
     * @param installStartTime
     * @param installFinishTime
     * @return
     */
    @PostMapping("selectTaskReports")
    public Result selectTaskReports(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            String taskName,
            String installStartTime,
            String installFinishTime
    ) {
        PageHelper.startPage(page, size);
        List<TaskReport> list = taskRecordService.selectTaskReports(taskName, installStartTime, installFinishTime);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 发送提醒消息
     * 比如：前道工序A忘了扫码，后面的工序B直接去扫码，此时需要提醒A去扫码完成。
     * MQTT 实例：
     * mqtt topic: /s2c/task_remind/1, msg: {"nameplate":"namePlate123"}  其中1是 taskName为上轴安装的安装组的groupId
     */
    @PostMapping("sendRemindMqttMsg")
    public Result sendRemindMqttMsg(@RequestParam String taskName,
                                    String nameplate) {
        String ret = commonService.sendMqttMsg(taskName,Constant.S2C_TASK_REMIND, nameplate);
        return ResultGenerator.genSuccessResult(ret);
    }

}
