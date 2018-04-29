package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.process_record.ProcessRecord;
import com.eservice.api.model.quality_record_image.QualityRecordImage;
import com.eservice.api.model.task.Task;
import com.eservice.api.model.task_quality_record.TaskQualityRecord;
import com.eservice.api.model.task_quality_record.TaskQualityRecordDetail;
import com.eservice.api.model.task_record.TaskRecord;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/11/24.
 */
@RestController
@RequestMapping("/task/quality/record")
public class TaskQualityRecordController {
    @Resource
    private TaskQualityRecordServiceImpl taskQualityRecordService;
    @Resource
    private QualityRecordImageServiceImpl qualityRecordImageService;
    @Resource
    private TaskRecordServiceImpl taskRecordService;
    @Resource
    private ProcessRecordServiceImpl processRecordService;
    @Resource
    private TaskServiceImpl taskService;
    @Resource
    private MachineServiceImpl machineService;
    @Resource
    private MachineOrderServiceImpl machineOrderService;
    @Resource
    private MqttMessageHelper mqttMessageHelper;
    /**
     * 质检异常管理excel表格，和合同excel表格放同个地方
     */
    @Value("${contract_excel_output_dir}")
    private String taskQualityRecordExcelOutputDir;

    @PostMapping("/add")
    public Result add(String taskQualityRecord) {
        TaskQualityRecord taskQualityRecord1 = JSON.parseObject(taskQualityRecord, TaskQualityRecord.class);
        taskQualityRecordService.save(taskQualityRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        taskQualityRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String taskQualityRecord) {
        TaskQualityRecord taskQualityRecord1 = JSON.parseObject(taskQualityRecord, TaskQualityRecord.class);
        taskQualityRecord1.setSolveTime(new Date());
        //修改对应工序的状态为“质检中”
        TaskQualityRecord completeInfo = taskQualityRecordService.findById(taskQualityRecord1.getId());
        Integer taskRecordId = completeInfo.getTaskRecordId();
        if (taskRecordId != null && taskRecordId > 0) {
            TaskRecord tr = taskRecordService.findById(taskRecordId);
            //MQTT 异常解决后，通知工序的质检员
            String taskName = tr.getTaskName();
            Condition condition = new Condition(Task.class);
            condition.createCriteria().andCondition("task_name = ", taskName);
            List<Task> taskList = taskService.findByCondition(condition);
            if (taskList == null || taskList.size() <= 0) {
                throw new RuntimeException();
            }
            tr.setStatus(Constant.TASK_QUALITY_DOING);
            taskRecordService.update(tr);

            ProcessRecord pr = processRecordService.findById(tr.getProcessRecordId());
            Machine machine = machineService.findById(pr.getMachineId());
            ServerToClientMsg msg = new ServerToClientMsg();
            MachineOrder machineOrder = machineOrderService.findById(machine.getOrderId());
            msg.setOrderNum(machineOrder.getOrderNum());
            msg.setNameplate(machine.getNameplate());
            mqttMessageHelper.sendToClient(Constant.S2C_QUALITY_ABNORMAL_RESOLVE + taskList.get(0).getQualityUserId(), JSON.toJSONString(msg));
        } else {
            throw new RuntimeException();
        }
        taskQualityRecordService.update(taskQualityRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        TaskQualityRecord taskQualityRecord = taskQualityRecordService.findById(id);
        return ResultGenerator.genSuccessResult(taskQualityRecord);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TaskQualityRecord> list = taskQualityRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据  task_record.id 返回QualityRecordDetail，包括 qurlity_record_image,task_quality_record等
     *
     * @param page
     * @param size
     * @param taskRecordId
     * @return
     */
    @PostMapping("/selectTaskQualityRecordDetails")
    public Result selectTaskQualityRecordDetail(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                                @RequestParam Integer taskRecordId) {
        PageHelper.startPage(page, size);
        List<TaskQualityRecordDetail> list = taskQualityRecordService.selectTaskQualityRecordDetails(taskRecordId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据异常类型、异常提交时间、提交者、解决者，返回abnormalRecordDetail
     *
     * @return
     */
    @PostMapping("/selectTaskQualityList")
    public Result selectTaskQualityList(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "0") Integer size,
                                        String nameplate,
                                        String orderNum,
                                        String taskName,
                                        Integer submitUser,
                                        Integer solutionUser,
                                        Integer finishStatus,
                                        String queryStartTime,
                                        String queryFinishTime) {
        PageHelper.startPage(page, size);
        List<TaskQualityRecordDetail> abnormalRecordDetailList = taskQualityRecordService.selectTaskQualityList(nameplate, orderNum, taskName, submitUser, solutionUser, finishStatus, queryStartTime, queryFinishTime);
        PageInfo pageInfo = new PageInfo(abnormalRecordDetailList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 生成 质检异常的excel表格
     */
    @PostMapping("/export")
    public Result export(
            String nameplate,
            String orderNum,
            String taskName,
            Integer submitUser,
            Integer solutionUser,
            Integer finishStatus,
            String queryStartTime,
            String queryFinishTime) {
        List<TaskQualityRecordDetail> list = taskQualityRecordService.selectTaskQualityList(nameplate, orderNum, taskName, submitUser, solutionUser, finishStatus, queryStartTime, queryFinishTime);

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
            wb = new HSSFWorkbook();
            Sheet sheet1 = wb.createSheet("sheet1");

            //设置标题行格式
            HSSFCellStyle headcellstyle = wb.createCellStyle();
            HSSFFont headfont = wb.createFont();
            headfont.setFontHeightInPoints((short) 10);
            headfont.setBold(true);
            headcellstyle.setFont(headfont);
            Row row;
            //创建行和列
            for (int r = 0; r < list.size() + 1; r++) {
                row = sheet1.createRow(r);
                for (int c = 0; c < 8; c++) {
                    row.createCell(c);
                    sheet1.getRow(0).getCell(c).setCellStyle(headcellstyle);
                }
            }
            sheet1.setColumnWidth(0, 1500);
            sheet1.setColumnWidth(1, 4000);
            sheet1.setColumnWidth(2, 4000);
            sheet1.setColumnWidth(3, 4000);
            sheet1.setColumnWidth(4, 4000);
            sheet1.setColumnWidth(5, 10000);
            sheet1.setColumnWidth(6, 4000);
            sheet1.setColumnWidth(7, 4000);
            //第一行为标题
            sheet1.getRow(0).getCell(0).setCellValue("序号");
            sheet1.getRow(0).getCell(1).setCellValue("机器编号");
            sheet1.getRow(0).getCell(2).setCellValue("工序");
            sheet1.getRow(0).getCell(3).setCellValue("提交者");
            sheet1.getRow(0).getCell(4).setCellValue("解决者");
            sheet1.getRow(0).getCell(5).setCellValue("解决方法");
            sheet1.getRow(0).getCell(6).setCellValue("创建时间");
            sheet1.getRow(0).getCell(7).setCellValue("解决时间");

            //第二行开始，填入值
            for (int r = 0; r < list.size(); r++) {
                row = sheet1.getRow(r + 1);
                row.getCell(0).setCellValue(r + 1);
                row.getCell(1).setCellValue(list.get(r).getMachine().getNameplate());
                row.getCell(2).setCellValue(list.get(r).getTaskRecord().getTaskName());
                row.getCell(3).setCellValue(list.get(r).getSubmitUser());

                //安装异常时， 还不知道SolutionUser， SolutionUser是null
                if (list.get(r).getSolutionUser() != null) {
                    row.getCell(4).setCellValue(list.get(r).getSolutionUser());
                }
                row.getCell(5).setCellValue(list.get(r).getSolution());
                dateString = formatter.format(list.get(r).getCreateTime());
                row.getCell(6).setCellValue(dateString);
                if (list.get(r).getSolveTime() != null) {
                    dateString = formatter.format(list.get(r).getSolveTime());
                    row.getCell(7).setCellValue(dateString);
                }
            }
            downloadPath = taskQualityRecordExcelOutputDir + "质检异常统计" + ".xls";
            downloadPathForNginx = "/excel/" + "质检异常统计" + ".xls";
            out = new FileOutputStream(downloadPath);
            wb.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ("".equals(downloadPath)) {
            return ResultGenerator.genFailResult("质检异常导出失败!");
        } else {
            return ResultGenerator.genSuccessResult(downloadPathForNginx);
        }
    }
}
