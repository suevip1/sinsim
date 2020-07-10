package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract.Equipment;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.MachineOrderServiceImpl;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.eservice.api.service.impl.OrderDetailServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/11/24.
*/
@RestController
@RequestMapping("/machine/order")
public class MachineOrderController {
    @Value("${order_report_output_dir}")
    private String reportOutputPath;
    
    @Resource
//    private MachineOrderService machineOrderService;
    private MachineOrderServiceImpl machineOrderService;

    @Resource
    private OrderDetailServiceImpl orderDetailService;

    @Resource
    private MachineServiceImpl machineService;

    private Logger logger = Logger.getLogger(MachineOrderController.class);
    /*
        为保证 MachineOrder表和OrderDetail表的一致性，MachineOrder表和OrderDetail表，都在这里统一完成
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public Result add(String  machineOrder, String orderDetail) {
        MachineOrder machineOrder1 = JSON.parseObject(machineOrder, MachineOrder.class);
        OrderDetail orderDetail1 = JSON.parseObject(orderDetail, OrderDetail.class);
        /**
         * 订单 不允许同名
         * 带下划线的字段，不能用findBy(fieldName,....)
         */
        try {
            Class cl = Class.forName("com.eservice.api.model.machine_order.MachineOrder");
            Field fieldOrderNum = cl.getDeclaredField("orderNum");

            MachineOrder mo = null;
            mo = machineOrderService.findBy(fieldOrderNum.getName(), machineOrder1.getOrderNum());
            if (mo != null) {
                logger.error( "/machine/order/add fail: 该 order_num 已存在 " +  machineOrder1.getOrderNum() );
                return ResultGenerator.genFailResult(machineOrder1.getOrderNum() + " 该 order_num 已存在");
            }
        } catch (ClassNotFoundException e) {
            logger.error( "/machine/order/add fail: " +e.getMessage());
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            logger.error( "/machine/order/add fail: " +e.getMessage());
            e.printStackTrace();
        }
        orderDetailService.saveAndGetID(orderDetail1);
        Integer savedOrderDetailID = orderDetail1.getId();

        //orderDetail里返回的id 就是machineOrder里的order_detail_id
        machineOrder1.setOrderDetailId(savedOrderDetailID);
        machineOrderService.save(machineOrder1);

        return ResultGenerator.genSuccessResult();
    }

    /*
    同时删除MachineOrder表和OrderDetail表的相关内容
     */
    @PostMapping("/delete")
    @Transactional (rollbackFor = Exception.class)
    public Result delete(@RequestParam Integer id) {
        Integer orderDetailID = machineOrderService.findById(id).getOrderDetailId();
        machineOrderService.deleteById(id);//先删除主表
        orderDetailService.deleteById(orderDetailID);//再删除外键所在的表
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String machineOrder) {
        MachineOrder machineOrder1 = JSON.parseObject(machineOrder,MachineOrder.class);
        machineOrderService.update(machineOrder1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        MachineOrder machineOrder = machineOrderService.findById(id);
        return ResultGenerator.genSuccessResult(machineOrder);
    }
	
    @PostMapping("/allDetail")
    public Result allDetail(@RequestParam Integer id) {
        MachineOrderDetail machineOrderDetail = machineOrderService.getOrderAllDetail(id);
        return ResultGenerator.genSuccessResult(machineOrderDetail);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<MachineOrder> list = machineOrderService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/getValidList")
    public Result getValidList(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);

        Condition condition = new Condition(MachineOrder.class);
        condition.createCriteria().andCondition("valid = ", 1);
        List<MachineOrder> list = machineOrderService.findByCondition(condition);

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /*
    根据条件查询订单。
    比如 查询 建立时间create_time在传入的参数 query_start_time 和 query_finish_time 之间的订单
     */
    //注意这个接口在不带参数时查询全部数据会有一定耗时，加了联系单查询。
    @PostMapping("/selectOrders")
    public Result selectOrders(
            @RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "0") Integer size,
            Integer id,
            Integer contract_id,
            String order_num,
            String contract_num,
            Integer status,
            String sellman,
            String customer,
            String marketGroupName,
            String query_start_time, //这个是查询创建日期
            String query_finish_time,//这个是查询创建日期
            String queryStartTimeSign, //这个是查询审核日期
            String queryFinishTimeSign,//这个是查询审核日期
            String machine_name,// 这个其实是机型
            @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page,size);
        List<MachineOrderDetail> list = machineOrderService.selectOrder(
                id,
                contract_id,
                order_num,
                contract_num,
                status,
                sellman,
                customer,
                marketGroupName,
                query_start_time,
                query_finish_time,
                queryStartTimeSign,
                queryFinishTimeSign,
                machine_name,
                is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //根据 参数orderNum返回订单
    @PostMapping("/getMachineOrder")
    public Result getMachineOrder(
            @RequestParam String orderNum ) {
         MachineOrder machineOrder = machineOrderService.getMachineOrder(orderNum);
        return ResultGenerator.genSuccessResult(machineOrder);
    }

    @PostMapping("/isOrderNumExist")
    public Result isOrderNumExist(@RequestParam String orderNum) {
        if (orderNum == null) {
            return ResultGenerator.genFailResult("请输入需求单编号！");
        } else {
            Condition condition = new Condition(MachineOrder.class);
            condition.createCriteria().andCondition("order_num = ", orderNum);
            List<MachineOrder> list = machineOrderService.findByCondition(condition);
            if (list.size() == 0) {
                return ResultGenerator.genSuccessResult();
            } else {
                return ResultGenerator.genFailResult("需求单编号已存在！");
            }
        }
    }

    @PostMapping("/updateValid")
    public Result updateValid(@RequestParam Integer orderId) {
        if (orderId == null || orderId <= 0) {
            return ResultGenerator.genFailResult("需求单对应ID不正确，请联系管理员！");
        }
        MachineOrder machineOrder = machineOrderService.findById(orderId);
        if (machineOrder == null) {
            return ResultGenerator.genFailResult("需求单编号不存在！");
        } else {
            //检查需求单对应的机器是否生成，如果生成则不能删除
            List<Machine> machineList = machineService.selectMachines(null,orderId,null,null,null,null,null,null,null,null, false);
            if(machineList.size() > 0) {
                //return ResultGenerator.genFailResult("需求单删除失败，对应机器已生成！");
                //机器已经生成，则把机器status设为 MACHINE_CANCELED
                for(int i=0;i<machineList.size();i++){
                    machineList.get(i).setStatus(Constant.MACHINE_CANCELED);
                    machineService.update(machineList.get(i));
                    logger.warn("订单被删除，已生成的机器 " + machineList.get(i).getNameplate() + " 设置为取消");
                }
            }

            machineOrder.setValid(Constant.ValidEnum.INVALID.getValue());
            machineOrder.setUpdateTime(new Date());
            machineOrderService.update(machineOrder);
            logger.warn(machineOrder.getOrderNum() + " 订单被删除");
            return ResultGenerator.genSuccessResult();

        }
    }

    @PostMapping("/exportToFinaceExcel")
    public Result exportToFinaceExcel(Integer id,
                                      Integer contract_id,
                                      String order_num,
                                      String contract_num,
                                      Integer status,
                                      String sellman,
                                      String customer,
                                      String marketGroupName,
                                      String query_start_time, //这个是查询创建日期
                                      String query_finish_time,//这个是查询创建日期
                                      String queryStartTimeSign, //这个是查询审核日期
                                      String queryFinishTimeSign,//这个是查询审核日期
                                      String machine_name,
                                      @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        List<MachineOrderDetail> list = machineOrderService.selectOrder(
                id,
                contract_id,
                order_num,
                contract_num,
                status,
                sellman,
                customer,
                marketGroupName,
                query_start_time,
                query_finish_time,
                machine_name,
                queryStartTimeSign,
                queryFinishTimeSign,is_fuzzy);

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
                Sheet sheet1=wb.createSheet("账务报表");
    
                //设置标题行格式
                HSSFCellStyle headcellstyle = wb.createCellStyle();
                HSSFFont headfont = wb.createFont();
                headfont.setFontHeightInPoints((short) 10);
                headfont.setBold(true);//粗体显示
                headcellstyle.setFont(headfont);
                Row row;
                row = sheet1.createRow(0);//新创建一行，行号为row+1
                for(int c=0; c<34; c++){//列头
                    row.createCell(c);//创建一个单元格，列号为col+1
                    sheet1.setColumnWidth(c,4500);
                    sheet1.getRow(0).getCell(c).setCellStyle(headcellstyle);
                }
              
                //第一行为标题
                sheet1.getRow(0).getCell(0).setCellValue("客户");
                sheet1.getRow(0).getCell(1).setCellValue("合同号");
                sheet1.getRow(0).getCell(2).setCellValue("订单号");
                sheet1.getRow(0).getCell(3).setCellValue("铭牌号");
                sheet1.getRow(0).getCell(4).setCellValue("机型");
                sheet1.getRow(0).getCell(5).setCellValue("台数");
                sheet1.getRow(0).getCell(6).setCellValue("单价");
                sheet1.getRow(0).getCell(7).setCellValue("装置");
                sheet1.getRow(0).getCell(8).setCellValue("装置总价");
                sheet1.getRow(0).getCell(9).setCellValue("优惠金额");
                sheet1.getRow(0).getCell(10).setCellValue("订单总价");
                sheet1.getRow(0).getCell(11).setCellValue("币种");
                sheet1.getRow(0).getCell(12).setCellValue("销售员"); 
                sheet1.getRow(0).getCell(13).setCellValue("销售费"); 
                sheet1.getRow(0).getCell(14).setCellValue("保修费"); 
                sheet1.getRow(0).getCell(15).setCellValue("保修人员"); 
                sheet1.getRow(0).getCell(16).setCellValue("付款方式"); 
                sheet1.getRow(0).getCell(17).setCellValue("定金率"); 
                sheet1.getRow(0).getCell(18).setCellValue("毛利"); 
                sheet1.getRow(0).getCell(19).setCellValue("包装方式"); 
                
                sheet1.getRow(0).getCell(20).setCellValue("机架长度"); 
                sheet1.getRow(0).getCell(21).setCellValue("针数"); 
                sheet1.getRow(0).getCell(22).setCellValue("头数"); 
                sheet1.getRow(0).getCell(23).setCellValue("头距"); 
                sheet1.getRow(0).getCell(24).setCellValue("X行程"); 
                sheet1.getRow(0).getCell(25).setCellValue("Y行程"); 
                sheet1.getRow(0).getCell(26).setCellValue("电脑"); 
                sheet1.getRow(0).getCell(27).setCellValue("剪线方式"); 
                sheet1.getRow(0).getCell(28).setCellValue("换色方式"); 
                sheet1.getRow(0).getCell(29).setCellValue("加油系统"); 
                sheet1.getRow(0).getCell(30).setCellValue("夹线器"); 
                sheet1.getRow(0).getCell(31).setCellValue("跳跃方式"); 
                sheet1.getRow(0).getCell(32).setCellValue("旋梭"); 
                sheet1.getRow(0).getCell(33).setCellValue("面线夹持"); 
                DataFormat dataFormat = wb.createDataFormat();
                CellStyle cellStyle;
                HSSFCellStyle wrapStyle=wb.createCellStyle();     
                wrapStyle.setWrapText(true);    
                //第二行开始，填入值
                for(int i=0; i<list.size(); i++ ) {
                    int r = i+1;
                    MachineOrderDetail mod=list.get(i);
                    row = sheet1.createRow(r);//新创建一行
                    for(int c=0; c<34; c++){
                        row.createCell(c);//创建列单元格
                    }
                    sheet1.getRow(r).getCell(0).setCellValue(mod.getCustomer());//客户
                    sheet1.getRow(r).getCell(1).setCellValue(mod.getContractNum());//合同号
                    sheet1.getRow(r).getCell(2).setCellValue(mod.getOrderNum());//
                    sheet1.getRow(r).getCell(0).setCellStyle(wrapStyle);
                    sheet1.getRow(r).getCell(1).setCellStyle(wrapStyle);
                    sheet1.getRow(r).getCell(2).setCellStyle(wrapStyle);

                    sheet1.getRow(r).getCell(3).setCellValue(mod.getNameplate());//
                    MachineType mt= mod.getMachineType();
                    if(mt!=null)
                    {
                        sheet1.getRow(r).getCell(4).setCellValue(mt.getName());//
                    }
                    sheet1.getRow(r).getCell(5).setCellValue(mod.getMachineNum());//
                    sheet1.getRow(r).getCell(6).setCellValue(mod.getMachinePrice());//
                    cellStyle=wb.createCellStyle();
                    cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));//金额格式
                    sheet1.getRow(r).getCell(6).setCellStyle(cellStyle);

                    List<Equipment> epArray = JSON.parseArray(mod.getEquipment(), Equipment.class);
                    String strEp="";
                    int epAmount=0;
                    for(Equipment itemEquipment:epArray)
                    {
                        strEp+=itemEquipment.getName()+":"+itemEquipment.getNumber()+"个"+"\r\n";
                        epAmount+=itemEquipment.getPrice()*itemEquipment.getNumber();
                    }
                   
                    sheet1.getRow(r).getCell(7).setCellStyle(wrapStyle);  
                    sheet1.getRow(r).getCell(7).setCellValue(new HSSFRichTextString(strEp));//装置

                    sheet1.getRow(r).getCell(8).setCellValue(epAmount);//装置金额
                    sheet1.getRow(r).getCell(8).setCellStyle(cellStyle);

                    sheet1.getRow(r).getCell(9).setCellValue(mod.getDiscounts());//优惠金额
                    Integer totalAmount=Integer.parseInt(mod.getMachinePrice())*mod.getMachineNum()
                    +epAmount - Integer.parseInt(mod.getDiscounts());
                    sheet1.getRow(r).getCell(9).setCellStyle(cellStyle);

                    sheet1.getRow(r).getCell(10).setCellValue(totalAmount);//总金额
                    sheet1.getRow(r).getCell(10).setCellStyle(cellStyle);

                    sheet1.getRow(r).getCell(11).setCellValue(mod.getCurrencyType());//币种
                    sheet1.getRow(r).getCell(12).setCellValue(mod.getSellman());//销售员
//                    sheet1.getRow(r).getCell(13).setCellValue(totalAmount);//销售费 先空着
//                    sheet1.getRow(r).getCell(14).setCellValue(0);//保修费 先空着
                    sheet1.getRow(r).getCell(15).setCellValue(mod.getMaintainPerson());//保修人员

                    sheet1.getRow(r).getCell(16).setCellValue(mod.getPayMethod());//付款方式
                    //sheet1.getRow(r).getCell(16).setCellStyle(wrapStyle);
//                    sheet1.getRow(r).getCell(17).setCellValue(0);//定金率 先空着
//                    sheet1.getRow(r).getCell(18).setCellValue(0);//毛利 先空着

                    sheet1.getRow(r).getCell(19).setCellValue(mod.getPackageMethod());//包装方式
                    sheet1.getRow(r).getCell(20).setCellValue("");//机架长度

                    OrderDetail od=mod.getOrderDetail();
                    sheet1.getRow(r).getCell(21).setCellValue(od.getAxleNeedle());//针数
                    sheet1.getRow(r).getCell(22).setCellValue(mod.getHeadNum());//头数
                    sheet1.getRow(r).getCell(23).setCellValue(mod.getHeadDistance());//头距
                    sheet1.getRow(r).getCell(24).setCellValue(mod.getxDistance());//X行程
                    sheet1.getRow(r).getCell(25).setCellValue(mod.getyDistance());//Y行程
                    sheet1.getRow(r).getCell(26).setCellValue(od.getElectricPc());//电脑

                    sheet1.getRow(r).getCell(27).setCellValue(od.getElectricTrim());//剪线方式
                    sheet1.getRow(r).getCell(28).setCellValue(od.getColorChangeMode());//换色方式
                    sheet1.getRow(r).getCell(29).setCellValue(od.getElectricOil());//加油系统
                    sheet1.getRow(r).getCell(30).setCellValue(od.getAxleSplit());//夹线器
                    sheet1.getRow(r).getCell(31).setCellValue(od.getAxleJump());//跳跃方式
                    sheet1.getRow(r).getCell(32).setCellValue(od.getAxleHook());//旋梭
                    sheet1.getRow(r).getCell(33).setCellValue(od.getAxleUpperThread());//面线夹持
                }
              
                downloadPath = reportOutputPath + "账务报表" + ".xls";
                downloadPathForNginx = "/report/" + "账务报表" + ".xls";
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

        @PostMapping("/exportToSaleExcel")
        public Result exportToSaleExcel(Integer id,
                                        Integer contract_id,
                                        String order_num,
                                        String contract_num,
                                        Integer status,
                                        String sellman,
                                        String customer,
                                        String marketGroupName,
                                        String query_start_time, //这个是查询创建日期
                                        String query_finish_time,//这个是查询创建日期
                                        String queryStartTimeSign, //这个是查询审核日期
                                        String queryFinishTimeSign,//这个是查询审核日期
                                        String machine_name,
                                        @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
                List<MachineOrderDetail> list = machineOrderService.selectOrder(
                        id,
                        contract_id,
                        order_num,
                        contract_num,
                        status,
                        sellman,
                        customer,
                        marketGroupName,
                        query_start_time,
                        query_finish_time,
                        machine_name,
                        queryStartTimeSign,
                        queryFinishTimeSign,
                        is_fuzzy);
    
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
                    Sheet sheet1=wb.createSheet("销售报表");
        
                    //设置标题行格式
                    HSSFCellStyle headcellstyle = wb.createCellStyle();
                    HSSFFont headfont = wb.createFont();
                    headfont.setFontHeightInPoints((short) 10);
                    headfont.setBold(true);//粗体显示
                    headcellstyle.setFont(headfont);
                    Row row;
                    row = sheet1.createRow(0);//新创建一行，行号为row+1
                    for(int c=0; c<15; c++){//列头
                        row.createCell(c);//创建一个单元格，列号为col+1
                        sheet1.setColumnWidth(c,4500);
                        sheet1.getRow(0).getCell(c).setCellStyle(headcellstyle);
                    }
                  
                    //第一行为标题
                    sheet1.getRow(0).getCell(0).setCellValue("客户");
                    sheet1.getRow(0).getCell(1).setCellValue("合同号");
                    sheet1.getRow(0).getCell(2).setCellValue("订单号");
                    sheet1.getRow(0).getCell(3).setCellValue("铭牌号");
                    sheet1.getRow(0).getCell(4).setCellValue("机型");
                    sheet1.getRow(0).getCell(5).setCellValue("台数");
                    sheet1.getRow(0).getCell(6).setCellValue("单价");
                    sheet1.getRow(0).getCell(7).setCellValue("装置");
                    sheet1.getRow(0).getCell(8).setCellValue("装置总价");
                    sheet1.getRow(0).getCell(9).setCellValue("优惠金额");
                    sheet1.getRow(0).getCell(10).setCellValue("订单总金额");
                    sheet1.getRow(0).getCell(11).setCellValue("币种");
                    sheet1.getRow(0).getCell(12).setCellValue("销售员"); 
                    sheet1.getRow(0).getCell(13).setCellValue("销售费"); 
                    sheet1.getRow(0).getCell(14).setCellValue("付款方式"); 
                    DataFormat dataFormat = wb.createDataFormat();
                    CellStyle cellStyle;
                    HSSFCellStyle wrapStyle=wb.createCellStyle();     
                    wrapStyle.setWrapText(true);     
                    //第二行开始，填入值
                    for(int i=0; i<list.size(); i++ ) {
                        int r = i+1;
                        MachineOrderDetail mod=list.get(i);
                        row = sheet1.createRow(r);//新创建一行
                        for(int c=0; c<15; c++){
                            row.createCell(c);//创建列单元格
                           
                        }
                        sheet1.getRow(r).getCell(0).setCellValue(mod.getCustomer());//客户
                        sheet1.getRow(r).getCell(1).setCellValue(mod.getContractNum());//合同号
                        sheet1.getRow(r).getCell(2).setCellValue(mod.getOrderNum());//
                        sheet1.getRow(r).getCell(0).setCellStyle(wrapStyle);
                        sheet1.getRow(r).getCell(1).setCellStyle(wrapStyle);
                        sheet1.getRow(r).getCell(2).setCellStyle(wrapStyle);

                        sheet1.getRow(r).getCell(3).setCellValue(mod.getNameplate());//
                        MachineType mt= mod.getMachineType();
                        if(mt!=null)
                        {
                            sheet1.getRow(r).getCell(4).setCellValue(mt.getName());//
                        }
                        sheet1.getRow(r).getCell(5).setCellValue(mod.getMachineNum());//
                        sheet1.getRow(r).getCell(6).setCellValue(mod.getMachinePrice());//
                        cellStyle=wb.createCellStyle();
                        cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));//金额格式
                        sheet1.getRow(r).getCell(6).setCellStyle(cellStyle);

                        List<Equipment> epArray = JSON.parseArray(mod.getEquipment(), Equipment.class);
                        String strEp="";
                        int epAmount=0;
                        for(Equipment itemEquipment:epArray)
                        {
                            strEp+=itemEquipment.getName()+":"+itemEquipment.getNumber()+"个"+"\r\n";
                            epAmount+=itemEquipment.getPrice()*itemEquipment.getNumber();
                        }

                       
                        sheet1.getRow(r).getCell(7).setCellStyle(wrapStyle); 
                        sheet1.getRow(r).getCell(7).setCellValue(new HSSFRichTextString(strEp));//装置
                        
                        sheet1.getRow(r).getCell(8).setCellValue(epAmount);//装置金额
                        sheet1.getRow(r).getCell(8).setCellStyle(cellStyle);
    
                        sheet1.getRow(r).getCell(9).setCellValue(mod.getDiscounts());//优惠金额
                        Integer totalAmount=Integer.parseInt(mod.getMachinePrice())* mod.getMachineNum()
                        +epAmount - Integer.parseInt(mod.getDiscounts());
                        sheet1.getRow(r).getCell(9).setCellStyle(cellStyle);

                        sheet1.getRow(r).getCell(10).setCellValue(totalAmount);//总金额
                        sheet1.getRow(r).getCell(10).setCellStyle(cellStyle);

                        sheet1.getRow(r).getCell(11).setCellValue(mod.getCurrencyType());//币种
                        sheet1.getRow(r).getCell(12).setCellValue(mod.getSellman());//销售员
//                        sheet1.getRow(r).getCell(13).setCellValue(totalAmount);//销售费 先空着
                        sheet1.getRow(r).getCell(13).setCellStyle(cellStyle);

                        sheet1.getRow(r).getCell(14).setCellValue(mod.getPayMethod());//付款方式
                        //sheet1.getRow(r).getCell(14).setCellStyle(wrapStyle);
                    }
                  
                    downloadPath = reportOutputPath + "销售报表" + ".xls";
                    downloadPathForNginx = "/report/" + "销售报表" + ".xls";
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
}
