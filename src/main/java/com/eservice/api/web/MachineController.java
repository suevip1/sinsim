package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine.MachinePlan;
import com.eservice.api.model.machine.MachineInfo;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.model.task_record.TaskRecordDetail;
import com.eservice.api.service.MachineTypeService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.eservice.api.service.impl.TaskRecordServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/12/26.
 */
@RestController
@RequestMapping("/machine")
public class MachineController {
    @Resource
    private MachineServiceImpl machineService;
    @Resource
    private TaskRecordServiceImpl taskRecordService;

    /**
     * 导出的excel表格，和合同excel表格放同个地方
     */
    @Value("${contract_excel_output_dir}")
    private String machinePorcessExcelOutputDir;

    @Resource
    private MachineTypeService machineTypeService;

    @PostMapping("/add")
    public Result add(String machine) {
        Machine machine1 = JSON.parseObject(machine, Machine.class);
        machineService.save(machine1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        machineService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/addMachineNum")
    public Result addMachineNum(String machine) {
        Machine machine1 = JSON.parseObject(machine, Machine.class);
        if (machine1.getNameplate() == null || machine1.getNameplate() == "") {
            return ResultGenerator.genFailResult("机器编号不能为空！");
        } else {
            //检查机器编号是否存在，存在则返回错误
            Condition condition = new Condition(Machine.class);
            condition.createCriteria().andCondition("nameplate = ", machine1.getNameplate());
            List<Machine> list = machineService.findByCondition(condition);
            if (list.size() >= 1) {
                return ResultGenerator.genFailResult("机器编号已存在！");
            }else {
                machineService.update(machine1);
                return ResultGenerator.genSuccessResult();
            }
        }
    }

    @PostMapping("/update")
    public Result update(String machine) {
        Machine machine1 = JSON.parseObject(machine, Machine.class);
        machine1.setUpdateTime(new Date());
        // 如果该机器有对应的工序是跳过未完成的，不允许设置为已完成。
        if(machine1.getNameplate()!= null && !machine1.getNameplate().isEmpty()) {
            List<TaskRecordDetail> list = taskRecordService.selectTaskRecordByMachineNameplate(machine1.getNameplate());
            for (int i = 0; i <list.size() ; i++) {
                if(list.get(i).getStatus().equals(Constant.TASK_SKIP)){
                    return ResultGenerator.genFailResult("该机器还有处于跳过状态的工序:" + list.get(i).getTaskName() + "，不允许设置为完成!");
                }
            }
        }
        machineService.update(machine1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Machine machine = machineService.findById(id);
        return ResultGenerator.genSuccessResult(machine);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Machine> list = machineService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectMachines")
    public Result selectMachines(@RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "0") Integer size,
                                 Integer id,
                                 Integer order_id,
                                 String machine_strid,
                                 String nameplate,
                                 String location,
                                 Byte status,
                                 Integer machine_type,
                                 String query_start_time,
                                 String query_finish_time,
                                 @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page, size);
        List<Machine> list = machineService.selectMachines(id, order_id, machine_strid, nameplate, location, status, machine_type, query_start_time, query_finish_time, is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectPlanningMachines")
    public Result selectPlanningMachines(@RequestParam(defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "0") Integer size,
                                         String orderNum,
                                         String machineStrId,
                                         String nameplate,
                                         String location,
                                         Byte status,
                                         Integer machineType,
                                         Integer dateType,
                                         String query_start_time,
                                         String query_finish_time,
                                         @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page, size);
        List<MachinePlan> list = machineService.selectPlanningMachines(orderNum, machineStrId, nameplate, location, status, machineType, dateType, query_start_time, query_finish_time, is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //
    @PostMapping("/selectConfigMachine")
    public Result selectConfigMachine(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            Integer order_id,
            String orderNum,
            String contractNum,
            String machine_strid,
            String nameplate,
            Integer machineType,
            String location,
            Byte status,
            String query_start_time,
            String query_finish_time,
            @RequestParam(defaultValue = "0") Integer configStatus,
            @RequestParam(defaultValue = "true") Boolean is_fuzzy
    ) {
        PageHelper.startPage(page, size);List<MachineInfo> list = machineService.selectConfigMachine(order_id, orderNum, contractNum, machine_strid, nameplate, machineType, location, status, query_start_time, query_finish_time, configStatus, is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectProcessMachine")
    public Result selectProcessMachine(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            Integer order_id,
            String orderNum,
            String contractNum,
            String machine_strid,
            String nameplate,
            String location,
            String status, ///支持多个状态用逗号隔开， "2,3,4"
            String query_start_time,
            String query_finish_time,
            String taskNameList, //工序集合，逗号分隔，支持UI按多个工序查询
            @RequestParam(defaultValue = "true") Boolean is_fuzzy
    ) {
        PageHelper.startPage(page, size);
        List<MachineInfo> list = machineService.selectProcessMachine(order_id, orderNum, contractNum, machine_strid, nameplate, location, status, query_start_time, query_finish_time,taskNameList, is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectMachinesByNameplate")
    public Result selectMachinesNameplate(String nameplate) {
        Machine machine = machineService.selectMachinesByNameplate(nameplate);
        return ResultGenerator.genSuccessResult(machine);
    }

    // todo: 安装进度 这个界面可能会改，如果改了这里也要相应地改。
    @PostMapping("/processMachineExport")
    public Result processMachineExport(
            Integer order_id,
            String orderNum,
            String contractNum,
            String machine_strid,
            String nameplate,
            String location,
            String status,
            String query_start_time,
            String query_finish_time,
            String taskNameList, //工序集合，逗号分隔，支持UI按多个工序查询
            @RequestParam(defaultValue = "true") Boolean is_fuzzy) {

        List<MachineInfo> list = machineService.selectProcessMachine(order_id, orderNum, contractNum, machine_strid, nameplate, location, status, query_start_time, query_finish_time,taskNameList, is_fuzzy);

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
                //序号，机器编号，机型，订单号，位置，当前工序，已完成/总工序，安装状态，开始时间，计划交货日期
                for(int c=0; c< 10; c++){
                    row.createCell(c);//创建一个单元格，列号为col+1
                    sheet1.getRow(0).getCell(c).setCellStyle(headcellstyle);
                    sheet1.setColumnWidth(c,4500);
                    sheet1.setColumnWidth(0,2500);
                }
            }

            //第一行为标题
            sheet1.getRow(0).getCell(0).setCellValue("序号");
            sheet1.getRow(0).getCell(1).setCellValue("机器编号");
            sheet1.getRow(0).getCell(2).setCellValue("机型");
            sheet1.getRow(0).getCell(3).setCellValue("订单号");
            sheet1.getRow(0).getCell(4).setCellValue("位置");
            sheet1.getRow(0).getCell(5).setCellValue("当前工序");
            sheet1.getRow(0).getCell(6).setCellValue("已完成/总工序");
            sheet1.getRow(0).getCell(7).setCellValue("安装状态");
            sheet1.getRow(0).getCell(8).setCellValue("开始时间");
            sheet1.getRow(0).getCell(9).setCellValue("计划交货日期");

            //第二行开始，填入值
            Machine machine = null;
            MachineOrder machineOrder = null;
            MachineType machineType1 = null;
            Byte machineStatus = 0;
            for(int r=0; r<list.size(); r++ ) {
                //序号，机器编号，机型，订单号，位置，当前工序，已完成/总工序，安装状态，开始时间，计划交货日期
                row = sheet1.getRow(r + 1);
                row.getCell(0).setCellValue(r + 1);

                //机器编号
                if(list.get(r).getMachineStrId() != null ) {
                    row.getCell(1).setCellValue(list.get(r).getNameplate());
                }
                //机型
                int machineTypeID = list.get(r).getMachineType();
//                machine = machineService.selectMachinesByNameplate(list.get(r).getNameplate());
                /**
                 * 获取机型类型的名称 machine_type.name
                 */
                machineType1 = machineTypeService.findById(machineTypeID);
                if(machineType1 != null) {
                    row.getCell(2).setCellValue(machineType1.getName()); //机型
                }
                //订单号
                if(list.get(r).getOrderNum() != null ) {
                    row.getCell(3).setCellValue(list.get(r).getOrderNum());
                }
                //位置
                if(list.get(r).getLocation() != null ) {
                    row.getCell(4).setCellValue(list.get(r).getLocation());
                }
                //当前工序
                ///todo: 从 nodeData中解析出工序？ 注意可能有多个。

                //已完成/总工序
                /// todo: 从 nodeData中解析计算?

                //状态
                machineStatus = list.get(r).getStatus();
                if(machineStatus == Constant.MACHINE_INITIAL) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_INITIAL);
                } else if(machineStatus == Constant.MACHINE_CONFIGURED) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_CONFIGURED);
                } else if(machineStatus == Constant.MACHINE_PLANING) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_PLANING);
                } else if(machineStatus == Constant.MACHINE_INSTALLING) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_INSTALLING);
                } else if(machineStatus == Constant.MACHINE_INSTALLED) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_INSTALLED);
                } else if(machineStatus == Constant.MACHINE_CHANGED) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_CHANGED);
                } else if(machineStatus == Constant.MACHINE_SPLITED) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_SPLITED);
                } else if(machineStatus == Constant.MACHINE_CANCELED) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_CANCELED);
                } else if(machineStatus == Constant.MACHINE_INSTALLING_INCLUDE_SKIP_TASK) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_INSTALLING_INCLUDE_SKIP_TASK);
                } else if(machineStatus == Constant.MACHINE_SHIPPED) {
                    row.getCell(7).setCellValue(Constant.STR_MACHINE_SHIPPED);
                }

                // 的开始时间
                if( list.get(r).getProcessCreateTime() != null) {
                    dateString = formatter2.format(list.get(r).getProcessCreateTime());
                    row.getCell(8).setCellValue(dateString);
                }
                //计划交货日期
                if( list.get(r).getPlanShipDate()!= null) {
                    dateString = formatter.format(list.get(r).getPlanShipDate());
                    row.getCell(9).setCellValue(dateString);
                }
            }
            downloadPath = machinePorcessExcelOutputDir + "导出安装进度" + ".xls";
            downloadPathForNginx = "/excel/" + "导出安装进度" + ".xls";
            out = new FileOutputStream(downloadPath);
            wb.write(out);
            out.close();
//
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
}
