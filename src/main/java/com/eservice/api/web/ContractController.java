package com.eservice.api.web;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract.ContractDetail;
import com.eservice.api.model.contract.MachineOrderWapper;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/07.
*/
@RestController
@RequestMapping("/contract")
public class ContractController {
    @Resource
    private ContractServiceImpl contractService;
    @Resource
    private ContractSignServiceImpl contractSignService;
    @Resource
    private MachineOrderServiceImpl machineOrderService;
    @Resource
    private OrderDetailServiceImpl orderDetailService;
    @Resource
    private OrderSignServiceImpl orderSignService;
    @Resource
    private CommonService commonService;

    @Value("${contract_excel_output_dir}")
    private String contractOutputDir;

    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public Result add(String contract, String contractSign, String requisitionForms) {
        if(contract == null || "".equals(contract)) {
            return ResultGenerator.genFailResult("合同信息为空！");
        }
        if(contractSign == null || "".equals(contractSign)) {
            return ResultGenerator.genFailResult("合同审核初始化信息为空！");
        }
        if(requisitionForms == null || "".equals(requisitionForms)) {
            return ResultGenerator.genFailResult("订单信息为空！");
        }
        Contract contract1 = JSONObject.parseObject(contract, Contract.class);
        if(contract1 == null) {
            return ResultGenerator.genFailResult("Contract对象JSON解析失败！");
        }
        contract1.setCreateTime(new Date());
        contractService.saveAndGetID(contract1);
        Integer contractId = contract1.getId();
        ///插入contract审核记录
        ContractSign contractSignObj = new ContractSign();
        contractSignObj.setContractId(contractId);
        contractSignObj.setCreateTime(new Date());
        contractSignObj.setSignContent(contractSign);
        ///新增合同签核记录时，插入空值
        contractSignObj.setCurrentStep("");
        contractSignObj.setStatus(Byte.parseByte("0"));
        contractSignService.save(contractSignObj);

        //插入需求单记录
        List<MachineOrderWapper> machineOrderWapperList = JSONObject.parseArray(requisitionForms, MachineOrderWapper.class);
        if(machineOrderWapperList != null) {
            for (int i = 0; i <machineOrderWapperList.size() ; i++) {
                OrderDetail temp = machineOrderWapperList.get(i).getOrderDetail();
                MachineOrder orderTemp = machineOrderWapperList.get(i).getMachineOrder();
                orderDetailService.saveAndGetID(temp);
                orderTemp.setOrderDetailId(temp.getId());
                orderTemp.setContractId(contractId);
                orderTemp.setStatus(Byte.parseByte("0"));
                machineOrderService.saveAndGetID(orderTemp);

                //初始化需求单审核记录
                OrderSign orderSignData = machineOrderWapperList.get(i).getOrderSign();
                OrderSign orderSign = new OrderSign();
                orderSign.setSignContent(orderSignData.getSignContent());
                orderSign.setOrderId(orderTemp.getId());
                orderSign.setCreateTime(new Date());
                orderSign.setStatus(Byte.parseByte("0"));
                orderSignService.save(orderSign);
            }
        }else {
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultGenerator.genFailResult("需求单为空！");
        }

        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        contractService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public Result update(String contract,  String requisitionForms) {
        Contract contract1 = JSONObject.parseObject(contract, Contract.class);
        List<MachineOrderWapper> machineOrderWapperlist = JSONObject.parseArray(requisitionForms,MachineOrderWapper.class );
        //先获取当前合同的所有订单
        List<MachineOrderDetail> originalOrderList =  machineOrderService.selectOrder(null, contract1.getId(), null, null, null,
                null, null, null,null, null, false);
        ///删除该合同下，不在本次保存范围内的需求单
        for (MachineOrderDetail item: originalOrderList) {
            boolean exist = false;
            for (MachineOrderWapper wapperItem: machineOrderWapperlist) {
                if(wapperItem.getMachineOrder().getId().equals(item.getId())) {
                    exist = true;
                    break;
                }
            }
            if(!exist) {
                //删除需求单审核记录
                OrderSign orderSign = orderSignService.findBy("orderId", item.getId());
                if(orderSign != null) {
                    orderSignService.deleteById(orderSign.getId());
                }
                //删除需求单
                machineOrderService.deleteById(item.getId());
                //删除detail
                orderDetailService.deleteById(item.getOrderDetailId());
            }
        }

        for (MachineOrderWapper item: machineOrderWapperlist) {
            if(item.getMachineOrder().getId() != null) {
                //更新
                OrderDetail temp = item.getOrderDetail();
                MachineOrder orderTemp = item.getMachineOrder();
                orderDetailService.update(temp);
                machineOrderService.update(orderTemp);
            }else {
                //新增
                OrderDetail temp = item.getOrderDetail();
                MachineOrder orderTemp = item.getMachineOrder();
                orderDetailService.saveAndGetID(temp);
                orderTemp.setOrderDetailId(temp.getId());
                orderTemp.setContractId(contract1.getId());
                orderTemp.setStatus(Byte.parseByte("0"));
                machineOrderService.saveAndGetID(orderTemp);

                //初始化需求单审核记录
                OrderSign orderSignData = item.getOrderSign();
                OrderSign orderSign = new OrderSign();
                orderSign.setSignContent(orderSignData.getSignContent());
                orderSign.setOrderId(orderTemp.getId());
                orderSign.setCreateTime(new Date());
                orderSign.setStatus(Byte.parseByte("0"));
                orderSignService.save(orderSign);
            }
        }

        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Contract contract = contractService.findById(id);
        return ResultGenerator.genSuccessResult(contract);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Contract> list = contractService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/selectContracts")
    public Result selectContracts(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ContractDetail> list = contractService.selectContracts();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/startSign")
    public Result startSign(@RequestParam Integer contractId) {
        if(contractId == null) {
            ResultGenerator.genFailResult("合同ID为空！");
        }else {
            ContractSign contractSign = contractSignService.findBy("contractId", contractId);
            if(contractSign == null) {
                return ResultGenerator.genFailResult("根据合同号获取合同签核信息失败！");
            }else {
                contractSign.setUpdateTime(new Date());
                //开始签核流程
                contractSign.setStatus(Byte.parseByte("1"));
                String currentStep =  commonService.getCurrentSignStep(contractId);
                if(currentStep == null) {
                    return ResultGenerator.genFailResult("获取当前签核steps失败！");
                }
                contractSign.setCurrentStep(currentStep);
                //设置完状态后更新签核记录
                contractSignService.update(contractSign);
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据 contract_id，创建EXCEL表格，“合同评审单”+“客户需求单” 等sheet。
     * 具体内容来自 contract, contract_sign,machine_order,order_detail
     * TODO: ...
     * @param contractId
     * @return
     */
    @PostMapping("/buildContractExcel")
    public Result buildContractExcel(@RequestParam Integer contractId) {
        InputStream fs = null;
        POIFSFileSystem pfs = null;
        HSSFWorkbook wb = null;
        FileOutputStream out = null;
        String downloadPath = "";
        try{
            ClassPathResource resource = new ClassPathResource("empty_contract.xls");
            fs = resource.getInputStream();
            pfs=new POIFSFileSystem(fs);
            wb = new HSSFWorkbook(pfs);

            //一个合同对应多个签核
            Contract contract = contractService.findById(contractId);
            List<Integer> contractSignIdList =  new ArrayList<Integer>();
            ContractSign contractSign;
            for (int i = 0; i <contractSignService.findAll().size() ; i++) {
                contractSign = contractSignService.findAll().get(i);
                contractSignIdList.add(contractSign.getContractId());
            }
            //一个合同可能对应多个需求单
            List<Integer> machineOrderIdList = new ArrayList<Integer>();
            MachineOrder mo;
            for (int i =0; i<machineOrderService.findAll().size() ; i++){
                mo = machineOrderService.findAll().get(i);
                if(mo.getContractId().equals(contractId)){
                    machineOrderIdList.add(mo.getId());
                }
            }
            MachineOrderDetail machineOrderDetail;

            //读取了模板内所有sheet内容
            HSSFSheet sheet1 = wb.getSheetAt(0);
            //在相应的单元格进行赋值(A2)
            HSSFCell cell = sheet1.getRow(1).getCell((short) 0);
            cell.setCellValue(new HSSFRichTextString( "合 同 号：" + contract.getContractNum() ));
            //D2
            cell = sheet1.getRow(1).getCell((short) 3);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(contract.getCreateTime());
            CellStyle style = cell.getCellStyle();
            style.setWrapText(true);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(dateString));
            //B3
            cell = sheet1.getRow(2).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(contract.getCustomerName()));

            //N个需求单，插入N行
            Integer machineOrderCount = machineOrderIdList.size();
            insertRow(wb,sheet1,5,machineOrderCount);

            System.out.println("======== machineOrderCount: " + machineOrderCount );
            Integer allSum = 0;
            for(int i=0; i<machineOrderCount;i++){

                machineOrderDetail = machineOrderService.getOrderAllDetail(machineOrderIdList.get(i));
                //A5,A6,A7,...品牌
                cell = sheet1.getRow(5+i).getCell((short) 0);
                cell.setCellValue(new HSSFRichTextString(machineOrderService.findAll().get(i).getBrand()));

                //B5,B6,B7,...机型
                cell = sheet1.getRow(5+i).getCell((short) 1);
                cell.setCellValue(new HSSFRichTextString( machineOrderDetail.getMachineType().getName()));

                //C5,C6,C7,...数量
                cell = sheet1.getRow(5+i).getCell((short) 2);
                cell.setCellValue(new HSSFRichTextString( machineOrderDetail.getMachineNum().toString()));

                //D5,D6,D7,...单价
                cell = sheet1.getRow(5+i).getCell((short) 3);
                cell.setCellValue(new HSSFRichTextString( machineOrderDetail.getMachinePrice()));

                //E5,E6,E7...总价
                cell = sheet1.getRow(5+i).getCell((short) 4);
                Integer sum = Integer.parseInt(machineOrderDetail.getMachinePrice()) *  machineOrderDetail.getMachineNum();
                allSum = allSum + sum;
                cell.setCellValue(new HSSFRichTextString( sum.toString() ));
            }

            Integer locationRow = 6 + machineOrderCount;
            // 总计
            cell = sheet1.getRow(locationRow++).getCell((short) 4);
            cell.setCellValue(new HSSFRichTextString( allSum.toString()));


            // 付款方式
            cell = sheet1.getRow(locationRow++).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString( contract.getPayMethod() ));

            // 合同交货日期
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            String dateTimeString = formatter.format(contract.getContractShipDate());
            cell = sheet1.getRow(locationRow++).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(dateTimeString));

            // 备注
            cell = sheet1.getRow(locationRow++).getCell((short) 0);
            cell.setCellValue(new HSSFRichTextString( contract.getMark()));

            // 销售员
            locationRow = locationRow+6;
            cell = sheet1.getRow(locationRow++).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString( contract.getSellman()));

            //修改模板内容导出新模板,生成路径供前端下载
            downloadPath = contractOutputDir + contract.getContractNum() + ".xls";
            out = new FileOutputStream(downloadPath);
            wb.write(out);
            out.close();

        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if("".equals(downloadPath)) {
            return ResultGenerator.genFailResult("生成合同文件失败!");
        }else {
            return ResultGenerator.genSuccessResult(downloadPath);
        }
    }

    private void insertRow(HSSFWorkbook wb, HSSFSheet sheet, int starRow,int rows) {
        sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows,true,false);
        starRow = starRow - 1;

        //创建 多 行
        for (int i = 0; i <rows; i++) {
            HSSFRow sourceRow = null;
            HSSFRow targetRow = null;
            HSSFCell sourceCell = null;
            HSSFCell targetCell = null;
            short m;
            starRow = starRow + 1;
            sourceRow = sheet.getRow(starRow);
            targetRow = sheet.createRow(starRow + 1);
            targetRow.setHeight(sourceRow.getHeight());

            //创建多列
            System.out.println(" rows: " + rows );
            for (m = sourceRow.getFirstCellNum(); m <5; m++) {

                targetCell = targetRow.createCell(m);
                sourceCell = sourceRow.getCell(m);
                targetCell.setCellStyle(sourceCell.getCellStyle());
                targetCell.setCellType(sourceCell.getCellType());
                System.out.println("========i: " + i +" ---------m: " + m);
            }
        }
    }

}
