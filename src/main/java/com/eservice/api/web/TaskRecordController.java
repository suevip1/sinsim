package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.abnormal_image.AbnormalImage;
import com.eservice.api.model.abnormal_record.AbnormalRecord;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.task_plan.TaskPlan;
import com.eservice.api.model.task_record.TaskRecord;
import com.eservice.api.model.task_record.TaskRecordDetail;
import com.eservice.api.service.MachineTypeService;
import com.eservice.api.service.UserService;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Value("${abnormal_images_saved_dir}")
    private String imagesSavedDir;
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
        PageInfo pageInfo = new PageInfo(list);

        InputStream fs = null;
        POIFSFileSystem pfs = null;
        HSSFWorkbook wb = null;
        FileOutputStream out = null;
        String downloadPath = "";
        /*
        返回给docker外部下载
         */
        String downloadPathForNginx = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
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
                    dateString = formatter.format(list.get(r).getInstallBeginTime());
                    row.getCell(6).setCellValue(dateString);
                }
                // 安装的结束时间
                if( list.get(r).getInstallEndTime() != null) {
                    dateString = formatter.format(list.get(r).getInstallEndTime());
                    row.getCell(7).setCellValue(dateString);
                }
                // 质检的开始时间
                if( list.get(r).getQualityBeginTime()  != null) {
                    dateString = formatter.format(list.get(r).getQualityBeginTime());
                    row.getCell(8).setCellValue(dateString);
                }
                // 质检的结束时间
                if( list.get(r).getQualityEndTime() != null) {
                    dateString = formatter.format(list.get(r).getQualityEndTime());
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
            downloadPathForNginx = "/excel/" + ".xls";
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

    @PostMapping("/updateTaskInfo")
    @Transactional(rollbackFor = Exception.class)
    public Result updateTaskInfo(String taskRecord) {
        TaskRecord tr = JSON.parseObject(taskRecord, TaskRecord.class);
        Integer id = tr.getId();
        if (id == null || id < 0) {
            return ResultGenerator.genFailResult("TaskRecord的ID为空，数据更新失败！");
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

        return ResultGenerator.genSuccessResult();
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
     */
    @PostMapping("/addTrArAi")
    public Result addTrArAi(@RequestParam String taskRecord,
                            @RequestParam String abnormalRecord,
                            @RequestParam String abnormalImage,
                            @RequestParam MultipartFile[] files) {

        //task_record(update)
        TaskRecord taskRecord1 = JSON.parseObject(taskRecord, TaskRecord.class);
        taskRecordService.update(taskRecord1);

        //abnormal_record add:
        AbnormalRecord abnormalRecord1 = JSON.parseObject(abnormalRecord, AbnormalRecord.class);
        abnormalRecordService.save(abnormalRecord1);

        //abnormal_image add:
        AbnormalImage abnormalImage1 = JSON.parseObject(abnormalImage,AbnormalImage.class);
        Integer abnormalRecordId = abnormalImage1.getAbnormalRecordId();
        File dir = new File(imagesSavedDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        String machineID = machineService.searchMachineByAbnormalRecordId(abnormalRecordId).getMachineStrId();
        if (machineID == null){
            return ResultGenerator.genFailResult("Error: no machine found by the abnormalRecordId, no records saved");
        }
        List<String> listResultPath = new ArrayList<>() ;
        for(int i=0; i<files.length; i++) {
            listResultPath.add( commonService.saveFile(imagesSavedDir, files[i], machineID, null, Constant.ABNORMAL_IMAGE));
        }
        if (listResultPath == null){
            return ResultGenerator.genFailResult("failed to save file, no records saved");
        } else {
            abnormalImage1.setImage(listResultPath.toString());
            abnormalImageService.save(abnormalImage1);
        }

        return ResultGenerator.genSuccessResult();
    }

}
