package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract.Equipment;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.machine_type.MachineType;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.*;
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
    private ContractServiceImpl contractService;

    @Resource
    private OrderSignServiceImpl orderSignService;

    @Resource
    private OrderDetailServiceImpl orderDetailService;

    @Resource
    private MachineServiceImpl machineService;

    @Resource
    private CommonService commonService;

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
        machineOrderService.saveAndGetID(machineOrder1);
        /**
         * 在产生订单时，自动生成设计单
         */
        commonService.createDesignDepInfo(machineOrder1);

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

    /**
     *   根据条件查询订单。
     * 比如 查询 建立时间create_time在传入的参数 query_start_time 和 query_finish_time 之间的订单
     * @param id
     * @param contract_id
     * @param order_num
     * @param contract_num
     * @param status   支持多个状态用逗号隔开， "2,3,4"
     * @param sellman
     * @param customer
     * @param marketGroupName
     * @param query_start_time
     * @param query_finish_time
     * @param queryStartTimeSign
     * @param queryFinishTimeSign
     * @param queryStartTimePlanShipDate, //这个是查询 计划日期（生产部的回复交期）
     * @param queryFinishTimePlanShipDate,//这个是查询 计划日期（生产部的回复交期）
     * @param machine_name
     * @param is_fuzzy
     * @return
     */
    //注意这个接口在不带参数时查询全部数据会有一定耗时，加了联系单查询。
    @PostMapping("/selectOrders")
    public Result selectOrders(
            @RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "0") Integer size,
            Integer id,
            Integer contract_id,
            String order_num,
            String contract_num,
            String status,
            String sellman,
            String customer,
            String marketGroupName,  // 订单归属部门  这个是自动限制的，根据角色自动限制查看哪些订单可见
            String query_start_time, //这个是查询创建日期
            String query_finish_time,//这个是查询创建日期
            String queryStartTimeSign, //这个是查询审核日期
            String queryFinishTimeSign,//这个是查询审核日期
            String machine_name,// 这个其实是机型
            String oderSignCurrentStep, //订单签核的当前步骤
            String searchDepartment, // 查询框里 查询部门，注意，这个和marketGroupName互不干涉
            String queryStartTimePlanShipDate, //这个是查询 计划日期（生产部的回复交期）
            String queryFinishTimePlanShipDate,//这个是查询 计划日期（生产部的回复交期）
            String needleNum,   //针数
            String headNum,     //头数
            @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page,size);

        //workaround
//        外贸经理：看外贸一部、二部订单，查询用词"外贸“ 两个字，后台已经模糊查询，匹配“外贸一部”和“外贸二部”
//        外贸总监：看外贸一部、二部订单，查询用词"外贸“ 两个字，后台已经模糊查询，匹配“外贸一部”和“外贸二部”
//        if (marketGroupName != null && !marketGroupName.isEmpty()) {
//            if (marketGroupName.equals(Constant.STR_DEPARTMENT_FOREIGN_FUZZY)) {
//                marketGroupName = Constant.STR_DEPARTMENT_FOREIGN_FUZZY;
//            }
//        }
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
                oderSignCurrentStep,
                searchDepartment,
                queryStartTimePlanShipDate,
                queryFinishTimePlanShipDate,
                needleNum,
                headNum,
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

    @PostMapping("/getMachineOrderByNameplate")
    public Result getMachineOrderByNameplate(
            @RequestParam String nameplate ) {
        MachineOrder machineOrder = machineOrderService.getMachineOrderByNameplate(nameplate);
        return ResultGenerator.genSuccessResult(machineOrder);
    }
	
    @PostMapping("/isOrderNumExist")
    public Result isOrderNumExist(@RequestParam String orderNum) {
        if (orderNum == null) {
            return ResultGenerator.genFailResult("请输入需求单编号！");
        } else {
            Condition condition = new Condition(MachineOrder.class);
            condition.createCriteria().andCondition("order_num = ", orderNum)
                    .andCondition("valid = ", 1);
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
                                      String status,
                                      String sellman,
                                      String customer,
                                      String marketGroupName,
                                      String query_start_time, //这个是查询创建日期
                                      String query_finish_time,//这个是查询创建日期
                                      String queryStartTimeSign, //这个是查询审核日期
                                      String queryFinishTimeSign,//这个是查询审核日期
                                      String machine_name,
                                      String oderSignCurrentStep, //订单签核的当前步骤
                                      String searchDepartment,
                                      String queryStartTimePlanShipDate, //这个是查询 计划日期（生产部的回复交期）
                                      String queryFinishTimePlanShipDate,//这个是查询 计划日期（生产部的回复交期）
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
                queryStartTimeSign,
                queryFinishTimeSign,
                machine_name,
                oderSignCurrentStep,
                searchDepartment,
                queryStartTimePlanShipDate,
                queryFinishTimePlanShipDate,
                null,
                null,
                is_fuzzy);

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
                Sheet sheet1=wb.createSheet("账务报表");
    
                //设置标题行格式
                HSSFCellStyle headcellstyle = wb.createCellStyle();
                HSSFFont headfont = wb.createFont();
                headfont.setFontHeightInPoints((short) 10);
                headfont.setBold(true);//粗体显示
                headcellstyle.setFont(headfont);
                Row row;
                row = sheet1.createRow(0);//新创建一行，行号为row+1
                int columnSum = 36;
                for(int c=0; c<columnSum; c++){//列头
                    row.createCell(c);//创建一个单元格，列号为col+1
                    sheet1.setColumnWidth(c,4500);
                    sheet1.getRow(0).getCell(c).setCellStyle(headcellstyle);
                }
              
                //第一行为标题
                int columnX = 0;
                sheet1.getRow(0).getCell(columnX++).setCellValue("客户");
                sheet1.getRow(0).getCell(columnX++).setCellValue("合同号");
                sheet1.getRow(0).getCell(columnX++).setCellValue("订单号");
//                sheet1.getRow(0).getCell(3).setCellValue("铭牌号");
                sheet1.getRow(0).getCell(columnX++).setCellValue("地区");
                sheet1.getRow(0).getCell(columnX++).setCellValue("机器信息");
                sheet1.getRow(0).getCell(columnX++).setCellValue("台数");
                sheet1.getRow(0).getCell(columnX++).setCellValue("单价");
                sheet1.getRow(0).getCell(columnX++).setCellValue("装置");
                sheet1.getRow(0).getCell(columnX++).setCellValue("装置总价");
                sheet1.getRow(0).getCell(columnX++).setCellValue("优惠金额");
                sheet1.getRow(0).getCell(columnX++).setCellValue("订单总价");
                sheet1.getRow(0).getCell(columnX++).setCellValue("币种");
                sheet1.getRow(0).getCell(columnX++).setCellValue("销售员");
                sheet1.getRow(0).getCell(columnX++).setCellValue("业务费"); // 销售费
                sheet1.getRow(0).getCell(columnX++).setCellValue("保修费");
                sheet1.getRow(0).getCell(columnX++).setCellValue("保修人员");
                sheet1.getRow(0).getCell(columnX++).setCellValue("付款方式");
                sheet1.getRow(0).getCell(columnX++).setCellValue("定金率");
                sheet1.getRow(0).getCell(columnX++).setCellValue("毛利");
                sheet1.getRow(0).getCell(columnX++).setCellValue("包装方式");
                
                sheet1.getRow(0).getCell(columnX++).setCellValue("机架长度");
                sheet1.getRow(0).getCell(columnX++).setCellValue("针数");
                sheet1.getRow(0).getCell(columnX++).setCellValue("头数");
                sheet1.getRow(0).getCell(columnX++).setCellValue("头距");
                sheet1.getRow(0).getCell(columnX++).setCellValue("X行程");
                sheet1.getRow(0).getCell(columnX++).setCellValue("Y行程");
                sheet1.getRow(0).getCell(columnX++).setCellValue("电脑");
                sheet1.getRow(0).getCell(columnX++).setCellValue("剪线方式");
                sheet1.getRow(0).getCell(columnX++).setCellValue("换色方式");
                sheet1.getRow(0).getCell(columnX++).setCellValue("加油系统");
                sheet1.getRow(0).getCell(columnX++).setCellValue("夹线器");
                sheet1.getRow(0).getCell(columnX++).setCellValue("跳跃方式");
                sheet1.getRow(0).getCell(columnX++).setCellValue("旋梭");
                sheet1.getRow(0).getCell(columnX++).setCellValue("面线夹持");
                sheet1.getRow(0).getCell(columnX++).setCellValue("订单类型");
                sheet1.getRow(0).getCell(columnX++).setCellValue("填表日期");
                DataFormat dataFormat = wb.createDataFormat();
                CellStyle cellStyle;
                HSSFCellStyle wrapStyle=wb.createCellStyle();     
                wrapStyle.setWrapText(true);    
                //第二行开始，填入值
                for(int i=0; i<list.size(); i++ ) {
                    int r = i+1;
                    MachineOrderDetail mod = list.get(i);
                    OrderDetail od = mod.getOrderDetail();
                    row = sheet1.createRow(r);//新创建一行
                    for(int c=0; c<columnSum; c++){
                        row.createCell(c);//创建列单元格
                    }

                    columnX = 0;
                    sheet1.getRow(r).getCell(columnX).setCellValue(mod.getCustomer());//客户
                    sheet1.getRow(r).getCell(columnX++).setCellStyle(wrapStyle);
                    sheet1.getRow(r).getCell(columnX).setCellValue(mod.getContractNum());//合同号
                    sheet1.getRow(r).getCell(columnX++).setCellStyle(wrapStyle);
                    sheet1.getRow(r).getCell(columnX).setCellValue(mod.getOrderNum());//
                    sheet1.getRow(r).getCell(columnX++).setCellStyle(wrapStyle);

                    if(mod.getDomesticTradeZone() == null || mod.getDomesticTradeZone().isEmpty()) {
                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getCountry());//地区-->国家
                    } else {
                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getDomesticTradeZone());//地区
                    }
//                    sheet1.getRow(r).getCell(3).setCellValue(mod.getNameplate());//
                    MachineType mt= mod.getMachineType();
                    if(mt!=null)
                    {   //机器信息
                        String machineInfo = mt.getName() +"/"
                                + mod.getNeedleNum() +"/"
                                + mod.getHeadNum() +"/"
                                + mod.getHeadDistance() +"/"
                                + mod.getxDistance() +"/"
                                + mod.getyDistance() +"/"
                                + mod.getElectricTrim() +"/"
                                + mod.getElectricPc();
                        sheet1.getRow(r).getCell(columnX++).setCellValue(machineInfo);//
                    }
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getMachineNum());//
                    sheet1.getRow(r).getCell(columnX).setCellValue(mod.getMachinePrice());//
                    cellStyle=wb.createCellStyle();
                    cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));//金额格式
                    sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);

                    List<Equipment> epArray = JSON.parseArray(mod.getEquipment(), Equipment.class);
                    String strEp="";
                    int epAmount=0;
                    for(Equipment itemEquipment:epArray)
                    {
                        strEp+=itemEquipment.getName()+":"+itemEquipment.getNumber()+"个"+"\r\n";
                        epAmount+=itemEquipment.getPrice()*itemEquipment.getNumber();
                    }
                   
                    sheet1.getRow(r).getCell(columnX).setCellStyle(wrapStyle);
                    sheet1.getRow(r).getCell(columnX++).setCellValue(new HSSFRichTextString(strEp));//装置

                    sheet1.getRow(r).getCell(columnX).setCellValue(epAmount);//装置金额
                    sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);

                    sheet1.getRow(r).getCell(columnX).setCellValue(mod.getDiscounts());//优惠金额
                    Double totalAmount=Double.parseDouble(mod.getMachinePrice())*mod.getMachineNum()
                    +epAmount - Double.parseDouble(mod.getDiscounts());
                    sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);

                    sheet1.getRow(r).getCell(columnX).setCellValue(totalAmount);//总金额
                    sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);

                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getCurrencyType());//币种
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getSellman());//销售员
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getBusinessExpense());//销售费
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getWarrantyFee());//保修费
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getMaintainPerson());//保修人员

                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getPayMethod());//付款方式
//                    sheet1.getRow(r).getCell(16).setCellValue(0);//定金率 先空着
                    // 改为要从成本核算员的意见中抽取 毛利率
                    columnX++;
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getGrossProfit());//毛利率

                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getPackageMethod());//包装方式

                    // 订单中关于技术部的意见里长度：***，显示在财务报表中，显示方式为【机架长度】一栏
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getMachineFrameLength());//机架长度

                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getNeedleNum());//针数
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getHeadNum());//头数
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getHeadDistance());//头距
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getxDistance());//X行程
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getyDistance());//Y行程
                    sheet1.getRow(r).getCell(columnX++).setCellValue(od.getElectricPc());//电脑

                    sheet1.getRow(r).getCell(columnX++).setCellValue(od.getElectricTrim());//剪线方式
                    sheet1.getRow(r).getCell(columnX++).setCellValue(od.getColorChangeMode());//换色方式
                    sheet1.getRow(r).getCell(columnX++).setCellValue(od.getElectricOil());//加油系统
                    sheet1.getRow(r).getCell(columnX++).setCellValue(od.getAxleSplit());//夹线器
                    sheet1.getRow(r).getCell(columnX++).setCellValue(od.getAxleJump());//跳跃方式
                    sheet1.getRow(r).getCell(columnX++).setCellValue(od.getAxleHook());//旋梭
                    sheet1.getRow(r).getCell(columnX++).setCellValue(od.getAxleUpperThread());//面线夹持
                    sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getOrderType());//订单类型
                    dateString = formatter.format(mod.getCreateTime());
                    sheet1.getRow(r).getCell(columnX++).setCellValue(dateString);//填表日期
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
                                        String status,
                                        String sellman,
                                        String customer,
                                        String marketGroupName,
                                        String query_start_time, //这个是查询创建日期
                                        String query_finish_time,//这个是查询创建日期
                                        String queryStartTimeSign, //这个是查询审核日期
                                        String queryFinishTimeSign,//这个是查询审核日期
                                        String machine_name,
                                        String oderSignCurrentStep,
                                        String searchDepartment,
                                        String queryStartTimePlanShipDate, //这个是查询 计划日期（生产部的回复交期）
                                        String queryFinishTimePlanShipDate,//这个是查询 计划日期（生产部的回复交期）
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
                        queryStartTimeSign,
                        queryFinishTimeSign,
                        machine_name,
                        oderSignCurrentStep,
                        searchDepartment,
                        queryStartTimePlanShipDate,
                        queryFinishTimePlanShipDate,
                        null,
                        null,
                        is_fuzzy);

                HSSFWorkbook wb = null;
                FileOutputStream out = null;
                String downloadPath = "";
                /*
                返回给docker外部下载
                    */
                String downloadPathForNginx = "";
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

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
                    int columnSum = 22;
                    for(int c=0; c<columnSum; c++){//列头
                        row.createCell(c);//创建一个单元格，列号为col+1
                        sheet1.setColumnWidth(c,4500);
                        sheet1.getRow(0).getCell(c).setCellStyle(headcellstyle);
                    }
                  
                    //第一行为标题
                    int columnX = 0;
                    sheet1.getRow(0).getCell(columnX++).setCellValue("客户");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("国家");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("部门");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("合同号");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("订单号");
//                    sheet1.getRow(0).getCell(3).setCellValue("铭牌号");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("机器信息");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("台数");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("单价");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("装置");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("装置数量");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("装置总价");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("优惠金额");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("订单总金额(美元)");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("订单总金额(人民币)");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("订单总金额(欧元)");
//                    sheet1.getRow(0).getCell(columnX++).setCellValue("币种");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("销售员");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("业务费"); //销售费
                    sheet1.getRow(0).getCell(columnX++).setCellValue("付款方式");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("毛利");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("订单类型");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("保修费");
                    sheet1.getRow(0).getCell(columnX++).setCellValue("签核更新日期");
                    DataFormat dataFormat = wb.createDataFormat();
                    CellStyle cellStyle;
                    HSSFCellStyle wrapStyle=wb.createCellStyle();     
                    wrapStyle.setWrapText(true);     
                    //第二行开始，填入值
                    int allOrdersEquipmentCount = 0;//各订单的装置数量-合计
                    int allOrdersEquipemntAmountCount = 0;//各订单的装置总价-合计
                    int allOrdersDiscountCount = 0;//各订单的优惠金额-合计
                    int allOrdersTotalPriceCount_USD = 0;//各订单订单总金额（美元）	合计
                    int allOrdersTotalPriceCount_RMB = 0;//各订单订单总金额（人民币）合计
                    int allOrdersTotalPriceCount_EUR= 0;//各订单订单总金额（欧元）合计

                    cellStyle=wb.createCellStyle();
                    cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));//金额格式
                    for(int i=0; i<list.size(); i++ ) {
                        String dateStringSignFinish = "未曾签核"; ///这里其实是最近一次签核的日期
                        int r = i+1;
                        MachineOrderDetail mod=list.get(i);
                        row = sheet1.createRow(r);//新创建一行
                        for(int c=0; c<columnSum; c++){
                            row.createCell(c);//创建列单元格
                           
                        }

                        //合同的内容也用到
                        Contract contract = contractService.findById(list.get(i).getContractId());
                        if(contract == null){
                            logger.error("异常，根据合同ID号，查找不到合同");
                            return ResultGenerator.genFailResult("异常，根据合同ID号，查找不到合同");
                        }
                        //签核也用到：签核完成时间
                        List<OrderSign> orderSignList = orderSignService.getOrderSignListByOrderId(mod.getOrderSign().getOrderId());
                        OrderSign orderSign = null;
                        List<SignContentItem> signContentItemList = null;
                        if (orderSignList.size() > 0) {
                            //订单的签核记录只有在新增合同-订单时，才创建订单的签核记录，所以这里 getOrderSignListByOrderId其实只返回一个签核记录
                            orderSign = orderSignList.get(orderSignList.size() - 1);
                            if(orderSign.getCurrentStep() == null ){
                                logger.warn("异常，orderSign.getCurrentStep()为null, 比如测试数据手动乱改动时可能出现");
                            }
                            if(orderSign.getCurrentStep() != null && orderSign.getCurrentStep().equals("签核完成")) {
                                //如果签核完成，取最后一个角色的签核时间
                                signContentItemList = JSON.parseArray(orderSign.getSignContent(), SignContentItem.class);
                                // 注意订单签核没有enable开关
                                if(signContentItemList != null )
                                    if(signContentItemList.size() != 0 ) { //改单等情况，不需要签核所以没有新的签核记录
                                        if (signContentItemList.get(signContentItemList.size() - 1).getDate() != null) {
                                            dateStringSignFinish = formatter.format(signContentItemList.get(signContentItemList.size() - 1).getDate());
                                        } else {
                                            logger.warn("signContentItemList.get(signContentItemList.size()-1).getDate() 是 null，比如测试数据手动乱改动时可能出现");
                                        }
                                    } else {
                                        dateStringSignFinish = "";
                                }
                            }

                        }
                        columnX = 0;
                        sheet1.getRow(r).getCell(columnX).setCellValue(mod.getCustomer());//客户
                        sheet1.getRow(r).getCell(columnX++).setCellStyle(wrapStyle);
                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getCountry());//国家
                        sheet1.getRow(r).getCell(columnX++).setCellValue(contract.getMarketGroupName());//部门
                        sheet1.getRow(r).getCell(columnX).setCellValue(mod.getContractNum());//合同号
                        sheet1.getRow(r).getCell(columnX++).setCellStyle(wrapStyle);
                        sheet1.getRow(r).getCell(columnX).setCellValue(mod.getOrderNum());//
                        sheet1.getRow(r).getCell(columnX++).setCellStyle(wrapStyle);

//                        sheet1.getRow(r).getCell(3).setCellValue(mod.getNameplate());//
                        MachineType mt= mod.getMachineType();
                        if(mt!=null)
                        {   //机器信息
                            String machineInfo = mt.getName() +"/"
                                    + mod.getNeedleNum() +"/"
                                    + mod.getHeadNum() +"/"
                                    + mod.getHeadDistance() +"/"
                                    + mod.getxDistance() +"/"
                                    + mod.getyDistance() +"/"
                                    + mod.getElectricTrim() +"/"
                                    + mod.getElectricPc();
                            sheet1.getRow(r).getCell(columnX++).setCellValue(machineInfo);//
                        }
                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getMachineNum());//
                        sheet1.getRow(r).getCell(columnX).setCellValue(mod.getMachinePrice());//
                        sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);

                        List<Equipment> epArray = JSON.parseArray(mod.getEquipment(), Equipment.class);
                        String strEp="";
                        int equipmentCount =0;//订单内  各种装置数量之和
                        int epPriceAmount=0;
                        for(Equipment itemEquipment:epArray)
                        {
                            strEp+=itemEquipment.getName()+":"+itemEquipment.getNumber()+"个"+"\r\n";
                            epPriceAmount+=itemEquipment.getPrice()*itemEquipment.getNumber();
                            equipmentCount += itemEquipment.getNumber();
                        }
                        allOrdersEquipmentCount += equipmentCount;
                        allOrdersEquipemntAmountCount +=epPriceAmount;
                       
//                        sheet1.getRow(r).getCell(columnX).setCellStyle(wrapStyle);
                        sheet1.getRow(r).getCell(columnX++).setCellValue(new HSSFRichTextString(strEp));//装置
                        sheet1.getRow(r).getCell(columnX++).setCellValue(equipmentCount);//装置数量
                        
                        sheet1.getRow(r).getCell(columnX).setCellValue(epPriceAmount);//装置总价
                        sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);
    
                        sheet1.getRow(r).getCell(columnX).setCellValue(mod.getDiscounts());//优惠金额
                        allOrdersDiscountCount += Integer.valueOf( mod.getDiscounts());
                        Double totalAmount = Double.parseDouble(mod.getMachinePrice())* mod.getMachineNum()
                                            + epPriceAmount *mod.getMachineNum()
                                            - Double.parseDouble(mod.getOrderTotalDiscounts())        //优惠金额 （需求单总价格优惠金额）
                                            - Double.parseDouble(mod.getDiscounts())*mod.getMachineNum();        //每台的优惠金额
//                                            - Double.parseDouble(mod.getIntermediaryPrice())*mod.getMachineNum();//每台的居间费用 和总价无关
                        sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);

                        if(mod.getCurrencyType().equals("美元")) {
                            allOrdersTotalPriceCount_USD += totalAmount;
                            sheet1.getRow(r).getCell(columnX).setCellValue(totalAmount);//总金额（美元）
                            sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);
                            columnX++;
                            columnX++; //
                        }
                        if(mod.getCurrencyType().equals("人民币")) {
                            allOrdersTotalPriceCount_RMB += totalAmount;
                            columnX++;
                            sheet1.getRow(r).getCell(columnX).setCellValue(totalAmount);//总金额（人民币）
                            sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);
                            columnX++;
                        }
                        if(mod.getCurrencyType().equals("欧元")) {
                            allOrdersTotalPriceCount_EUR += totalAmount;
                            columnX++;
                            columnX++;
                            sheet1.getRow(r).getCell(columnX).setCellValue(totalAmount);//总金额（欧元）
                            sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);
                        }

//                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getCurrencyType());//币种
                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getSellman());//销售员
                        sheet1.getRow(r).getCell(columnX).setCellValue(mod.getBusinessExpense());//销售费 业务费
                        sheet1.getRow(r).getCell(columnX++).setCellStyle(cellStyle);

                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getPayMethod());//付款方式
                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getGrossProfit());//毛利率
                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getOrderType());//订单类型
                        sheet1.getRow(r).getCell(columnX++).setCellValue(mod.getWarrantyFee());//保修费
                        sheet1.getRow(r).getCell(columnX++).setCellValue(dateStringSignFinish);//签核完成时间

                    }
                    //最后一行 汇总
                    Row rowSum = sheet1.createRow(list.size()+1);//新创建一行
                    for(int c=0; c<columnSum; c++){
                        rowSum.createCell(c);//创建列单元格

                    }
                    rowSum.getCell(9).setCellValue(allOrdersEquipmentCount);//各订单的装置数量-合计
                    rowSum.getCell(10).setCellValue(allOrdersEquipemntAmountCount);//各订单的装置总价 合计
                    rowSum.getCell(10).setCellStyle(cellStyle);
                    rowSum.getCell(11).setCellValue(allOrdersDiscountCount);//优惠金额  合计
                    rowSum.getCell(12).setCellValue(allOrdersTotalPriceCount_USD);//订单总金额（美元）	合计
                    rowSum.getCell(12).setCellStyle(cellStyle);
                    rowSum.getCell(13).setCellValue(allOrdersTotalPriceCount_RMB);//订单总金额（人民币）合计
                    rowSum.getCell(13).setCellStyle(cellStyle);
                    rowSum.getCell(14).setCellValue(allOrdersTotalPriceCount_EUR);//订单总金额（欧元）合计
                    rowSum.getCell(14).setCellStyle(cellStyle);

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
