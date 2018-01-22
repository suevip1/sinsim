package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract.ContractDetail;
import com.eservice.api.model.contract.MachineOrderWrapper;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_change_record.OrderChangeRecord;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.service.OrderChangeRecordService;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
    @Resource
    private OrderChangeRecordServiceImpl orderChangeRecordService;
    @Resource
    private MachineServiceImpl machineService;

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
        contractSignService.save(contractSignObj);

        //插入需求单记录
        List<MachineOrderWrapper> machineOrderWapperList = JSONObject.parseArray(requisitionForms, MachineOrderWrapper.class);
        if(machineOrderWapperList != null) {
            for (int i = 0; i <machineOrderWapperList.size() ; i++) {
                OrderDetail temp = machineOrderWapperList.get(i).getOrderDetail();
                MachineOrder orderTemp = machineOrderWapperList.get(i).getMachineOrder();
                orderDetailService.saveAndGetID(temp);
                orderTemp.setOrderDetailId(temp.getId());
                orderTemp.setContractId(contractId);
                orderTemp.setStatus(Constant.ORDER_INITIAL);
                machineOrderService.saveAndGetID(orderTemp);

                //初始化需求单审核记录
                OrderSign orderSignData = machineOrderWapperList.get(i).getOrderSign();
                OrderSign orderSign = new OrderSign();
                orderSign.setSignContent(orderSignData.getSignContent());
                orderSign.setOrderId(orderTemp.getId());
                orderSign.setCreateTime(new Date());
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
        List<MachineOrderWrapper> machineOrderWapperlist = JSONObject.parseArray(requisitionForms,MachineOrderWrapper.class );
        //先获取当前合同的所有订单
        List<MachineOrderDetail> originalOrderList =  machineOrderService.selectOrder(null, contract1.getId(), null, null, null,
                null, null, null,null, null, false);
        ///删除该合同下，不在本次保存范围内的需求单
        for (MachineOrderDetail item: originalOrderList) {
            boolean exist = false;
            for (MachineOrderWrapper wapperItem: machineOrderWapperlist) {
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

        for (MachineOrderWrapper item: machineOrderWapperlist) {
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
                orderTemp.setStatus(Constant.ORDER_INITIAL);
                machineOrderService.saveAndGetID(orderTemp);

                //初始化需求单审核记录
                OrderSign orderSignData = item.getOrderSign();
                OrderSign orderSign = new OrderSign();
                orderSign.setSignContent(orderSignData.getSignContent());
                orderSign.setOrderId(orderTemp.getId());
                orderSign.setCreateTime(new Date());
                orderSignService.save(orderSign);
            }
        }
        //前端只要操作了“保存”，合同的状态回到“CONTRACT_INITIAL”状态
        contract1.setStatus(Constant.CONTRACT_INITIAL);
        contractService.update(contract1);

        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/changeOrder")
    @Transactional(rollbackFor = Exception.class)
    public Result changeOrder(String contract, String contractSign, String requisitionForms) {
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


        //更改合同的状态为“改单”
        contract1.setStatus(Constant.CONTRACT_CHANGED);
        Integer contractId = contract1.getId();
        ///插入新的contract审核记录
        ContractSign contractSignObj = new ContractSign();
        contractSignObj.setContractId(contractId);
        contractSignObj.setCreateTime(new Date());
        contractSignObj.setSignContent(contractSign);
        ///插入空值
        contractSignObj.setCurrentStep("");
        contractSignService.save(contractSignObj);

        //新增的改单处理
        List<MachineOrderWrapper> machineOrderWrapperList = JSONObject.parseArray(requisitionForms,MachineOrderWrapper.class);
        for (MachineOrderWrapper orderItem: machineOrderWrapperList) {
            MachineOrder machineOrder = orderItem.getMachineOrder();
            if(machineOrder.getOrderDetailId() != null && machineOrder.getOriginalOrderId() != 0) {
                //插入新增改单项的detail
                OrderDetail temp = orderItem.getOrderDetail();
                orderDetailService.saveAndGetID(temp);
                machineOrder.setOrderDetailId(temp.getId());
                machineOrder.setContractId(contract1.getId());
                machineOrder.setStatus(Constant.ORDER_INITIAL);
                machineOrderService.saveAndGetID(machineOrder);

                //初始化需求单审核记录
                OrderSign orderSignData = orderItem.getOrderSign();
                OrderSign orderSign = new OrderSign();
                orderSign.setSignContent(orderSignData.getSignContent());
                orderSign.setOrderId(machineOrder.getId());
                orderSign.setCreateTime(new Date());
                orderSignService.save(orderSign);

                //改单记录(插入或者修改)
                OrderChangeRecord changeRecord = orderItem.getOrderChangeRecord();
                if(changeRecord.getId() == null) {
                    changeRecord.setChangeTime(new Date());
                    orderChangeRecordService.save(changeRecord);
                }else {
                    changeRecord.setChangeTime(new Date());
                    orderChangeRecordService.update(changeRecord);
                }
            }
        }

        for (MachineOrderWrapper orderItem: machineOrderWrapperList) {
            MachineOrder machineOrder = orderItem.getMachineOrder();
            //设置被改单的需求单状态(machine_order/order_sign)
            if (machineOrder.getStatus().equals(Constant.ORDER_CHANGED)) {
                //更新了被改的需求单为“改单”，持久化至数据库
                machineOrderService.update(machineOrder);
                //获取被改单对应机器，设置改单状态(machine)
                Condition tempCondition = new Condition(Machine.class);
                tempCondition.createCriteria().andCondition("order_id = ", machineOrder.getId());
                List<Machine> machineList = machineService.findByCondition(tempCondition);
                //寻找对应新需求单，比较机器数
                MachineOrder newOrder = null;
                for (MachineOrderWrapper wrapper : machineOrderWrapperList) {
                    if (wrapper.getMachineOrder().getOriginalOrderId().equals(machineOrder.getId())) {
                        newOrder = wrapper.getMachineOrder();
                        break;
                    }
                }
                if (newOrder != null) {
                    ///改单前后机器数相等或者大于原需求单数中对应的机器数;多出部分机器在审核完成以后自动添加
                    if (newOrder.getMachineNum() >= machineOrder.getMachineNum()) {
                        for (Machine machine : machineList) {
                            ///初始化状态，直接将机器的上的需求单号直接绑定到新需求单
                            if (machine.getStatus().equals(Constant.MACHINE_INITIAL)) {
                            } else {
                                machine.setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CHANGED)));
                            }
                            machine.setOrderId(newOrder.getId());
                            machine.setUpdateTime(new Date());
                            machineService.update(machine);
                        }
                    } else {
                        List<Machine> originalInitialMachine = new ArrayList<>();
                        List<Machine> originalInitialedMachine = new ArrayList<>();
                        List<Machine> originalOtherMachine = new ArrayList<>();
                        for (Machine machine : machineList) {
                            if (machine.getStatus().equals(Constant.MACHINE_PLANING)
                                    || machine.getStatus().equals( Constant.MACHINE_INSTALLING)
                                    || machine.getStatus().equals(Constant.MACHINE_INSTALLED)
                                    || machine.getStatus().equals(Constant.MACHINE_SPLITED)) {
                                ///查找计划中、生产中、被拆单、生产完成的机器
                                originalInitialedMachine.add(machine);
                            } else if (machine.getStatus().equals(Constant.MACHINE_INITIAL)) {
                                ///初始化状态，未开始计划
                                originalInitialMachine.add(machine);
                            } else {
                                originalOtherMachine.add(machine);
                            }
                        }
                        int addedNum = 0;
                        //生产中的机器优先处理
                        for (int i = 0; i < originalInitialedMachine.size(); i++) {
                            if (addedNum < newOrder.getMachineNum()) {
                                originalInitialedMachine.get(i).setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CHANGED)));
                                addedNum++;
                            } else {
                                originalInitialedMachine.get(i).setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CANCELED)));
                            }
                            originalInitialedMachine.get(i).setOrderId(newOrder.getId());
                            //更新
                            machineService.update(originalInitialedMachine.get(i));
                        }
                        //未计划的机器其次处理
                        for (int i = 0; i < originalInitialMachine.size(); i++) {
                            if (addedNum < newOrder.getMachineNum()) {
                                ///TODO：是否可以保持“MACHINE_INITIAL”状态不变，因为此时机器还没有计划，也就是说没有对其设置安装流程
                                //originalInitialMachine.get(i).setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CHANGED)));
                                addedNum++;
                                originalInitialMachine.get(i).setOrderId(newOrder.getId());
                                machineService.update(originalInitialMachine.get(i));
                            } else {
                                //删除
                                machineService.deleteById(originalInitialMachine.get(i).getId());
                            }
                        }

                        //目前只有删除状态的机器（MACHINE_CANCELED）
                        for (int i = 0; i < originalOtherMachine.size(); i++) {
                            if (addedNum < newOrder.getMachineNum()) {
                                originalOtherMachine.get(i).setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CHANGED)));
                                originalOtherMachine.get(i).setOrderId(newOrder.getId());
                                machineService.update(originalInitialMachine.get(i));
                                addedNum++;
                            } else {
                                originalOtherMachine.get(i).setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CANCELED)));
                                machineService.update(originalInitialMachine.get(i));
                            }
                        }
                    }
                } else {
                    ///在同一个合同中没有找到新的需求单,抛出异常
                    throw new RuntimeException();
                }
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
    @Transactional(rollbackFor = Exception.class)
    public Result startSign(@RequestParam Integer contractId) {
        if(contractId == null) {
            ResultGenerator.genFailResult("合同ID为空！");
        }else {
            ContractSign contractSign = contractSignService.detailByContractId(String.valueOf(contractId));
            if(contractSign == null) {
                return ResultGenerator.genFailResult("根据合同号获取合同签核信息失败！");
            }else {
                //更新合同状态为“CONTRACT_CHECKING”
                Contract contract = contractService.findById(contractId);
                if(contract == null) {
                    return ResultGenerator.genFailResult("合同编号ID无效");
                }else {
                    contract.setStatus(Constant.CONTRACT_CHECKING);
                    contractService.update(contract);
                }

                //设置该合同下的需求单状态，如果处于“ORDER_INITIAL”状态，则设置为“ORDER_CHECKING”
                Condition tempCondition = new Condition(MachineOrder.class);
                tempCondition.createCriteria().andCondition("contract_id = ", contractId);
                tempCondition.createCriteria().andCondition("status = ", Constant.ORDER_INITIAL);
                List<MachineOrder> orderList = machineOrderService.findByCondition(tempCondition);
                for (MachineOrder orderItem: orderList) {
                    orderItem.setStatus(Constant.ORDER_CHECKING);
                    machineOrderService.update(orderItem);
                }

                //更新签核记录
                contractSign.setUpdateTime(new Date());
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

            Contract contract = contractService.findById(contractId);
            if(contract == null){
                return ResultGenerator.genFailResult("contractID not exist!");
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
            MachineOrder machineOrder;
            MachineOrderDetail machineOrderDetail;
            //需求单签核,一个需求单对应0个或多个签核
            List<OrderSign> orderSignList ;

            //读取了模板内所有sheet1内容
            HSSFSheet sheet1 = wb.getSheetAt(0);
            //在相应的单元格进行赋值(A2)
            HSSFCell cell = sheet1.getRow(1).getCell((short) 0);
            cell.setCellValue(new HSSFRichTextString( "合 同 号：" + contract.getContractNum() ));
            //D2
            cell = sheet1.getRow(1).getCell((short) 3);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");
            String dateString = formatter.format(contract.getCreateTime());
            HSSFCellStyle style = cell.getCellStyle();
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
                machineOrder = machineOrderService.findById(contractId);
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

            //一个合同对应多个签核 TODO:多个签核时如何选择，contractSignService.detailByContractId 可能要改。
            ContractSign contractSign;
            // 合同审核信息，来自 contract_sign
            contractSign = contractSignService.detailByContractId(contractId.toString());
            SignContentItem signContentItem;
            String contractSignCommentBySalesDep = "";
            String contractSignCommentByCostAccount = "";
            String contractSignCommentByFinancialDepRuleCheck = "";
            String contractSignCommentByGM = "";
            String contractSignUserBySaleDep = "";
            String contractSignUserByCostAccount = "";
            String contractSignUserByFinancialDepRuleCheck = "";
            String contractSignUserByGM = "";
            String contractSignDateBySalesDep = "";
            String contractSignDateByCostAccount = "";
            String contractSignDateByFinancialDepRuleCheck = "";
            String contractSignDateByGM = "";
            List<SignContentItem> signContentItemList = JSON.parseArray(contractSign.getSignContent(), SignContentItem.class);

            OrderSign orderSign = null;
            // 合同审核信息，来自 contract_sign
            String OderSignCommentByTechDep = "";
            String OrderSignCommentByPMC = "";
            String OrderSignCommentByFinancialDeposit = "";

            String orderSignUserByTechDep = "";
            String orderSignUserByPMC = "";
            String orderSignUserByFinancialDeposit = "";
            String orderSignDateByTechDep ="";
            String orderSignDateByPMC ="";
            String orderSignDateByFinancialDeposit ="";

            for ( int i=0;i<signContentItemList.size();i++ ) {
                signContentItem = signContentItemList.get(i);
                if(Constant.SALES_DEP_STEP.equals( signContentItem.getNumber())){
                    //销售部签核顺位
                    contractSignCommentBySalesDep = signContentItem.getComment();
                    contractSignUserBySaleDep = signContentItem.getUser();
                    if(signContentItem.getDate() != null) {
                        contractSignDateBySalesDep = formatter2.format(signContentItem.getDate());
                    }
                } else if(Constant.COST_ACCOUNT_STEP.equals( signContentItem.getNumber()) ) {
                    //成本核算
                    contractSignCommentByCostAccount = signContentItem.getComment();
                    contractSignUserByCostAccount = signContentItem.getUser();
                    if(signContentItem.getDate() != null) {
                        contractSignDateByCostAccount = formatter2.format(signContentItem.getDate());
                    }
                } else if(Constant.FINANCIAL_DEP_RULE_STEP.equals( signContentItem.getNumber()) ) {
                    //财务rule审核
                    contractSignCommentByFinancialDepRuleCheck = signContentItem.getComment();
                    contractSignUserByFinancialDepRuleCheck = signContentItem.getUser();
                    if(signContentItem.getDate() != null) {
                        contractSignDateByFinancialDepRuleCheck = formatter2.format(signContentItem.getDate());
                    }
                } else if(Constant.GENERAL_MANAGER_STEP.equals( signContentItem.getNumber()) ) {
                    //总经理审核
                    contractSignCommentByGM = signContentItem.getComment();
                    contractSignUserByGM = signContentItem.getUser();
                    if(signContentItem.getDate() != null) {
                        contractSignDateByGM = formatter2.format(signContentItem.getDate());
                    }
                }
            }
            //销售部
            cell = sheet1.getRow(locationRow).getCell((short) 4);
            cell.setCellValue(new HSSFRichTextString(contractSignCommentBySalesDep));
            cell = sheet1.getRow(locationRow).getCell((short)1);
            cell.setCellValue(new HSSFRichTextString(contractSignUserBySaleDep));
            cell = sheet1.getRow(locationRow++).getCell((short)2);
            cell.setCellValue(new HSSFRichTextString(contractSignDateBySalesDep));

            //成本审核
            cell = sheet1.getRow(locationRow).getCell((short) 4);
            cell.setCellValue(new HSSFRichTextString(contractSignCommentByCostAccount));
            cell = sheet1.getRow(locationRow).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(contractSignUserByCostAccount));
            cell = sheet1.getRow(locationRow++).getCell((short)2);
            cell.setCellValue(new HSSFRichTextString(contractSignDateByCostAccount));

            //财务审核
            cell = sheet1.getRow(locationRow).getCell((short) 4);
            cell.setCellValue(new HSSFRichTextString(contractSignCommentByFinancialDepRuleCheck));
            cell = sheet1.getRow(locationRow).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(contractSignUserByFinancialDepRuleCheck));
            cell = sheet1.getRow(locationRow++).getCell((short)2);
            cell.setCellValue(new HSSFRichTextString(contractSignDateByFinancialDepRuleCheck));

            //总经理
            cell = sheet1.getRow(locationRow).getCell((short) 4);
            cell.setCellValue(new HSSFRichTextString(contractSignCommentByGM));
            cell = sheet1.getRow(locationRow).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(contractSignUserByGM));
            cell = sheet1.getRow(locationRow++).getCell((short)2);
            cell.setCellValue(new HSSFRichTextString(contractSignDateByGM));

            //需求单
            //根据实际需求单数量，动态复制生成新的sheet;
            for(int i=0; i<machineOrderCount-1;i++) {
                // clone已经包含copy+paste
                wb.cloneSheet(1);
            }
            //调整sheet位置
            Integer sheetCount = wb.getNumberOfSheets();
            wb.setSheetOrder("Sheet3",sheetCount-1);

            //sheet2，sheet3...,第1,2,...个需求单
            for(int i=0; i<machineOrderCount;i++) {
                machineOrderDetail = machineOrderService.getOrderAllDetail(machineOrderIdList.get(i));

                HSSFSheet sheetX = wb.getSheetAt(1+i);
                //在相应的单元格进行赋值(C2)
                HSSFCell cell2 = sheetX.getRow(1).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( contract.getContractNum() ));
                //E2
                cell2 = sheetX.getRow(1).getCell((short) 4);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderNum() ));
                //H2
                cell2 = sheetX.getRow(1).getCell((short) 7);
                style.setWrapText(true);
                cell2.setCellStyle(style);
                cell2.setCellValue(new HSSFRichTextString(dateString));

                //C3
                cell2 = sheetX.getRow(2).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getCustomer()));
                //E3
                cell2 = sheetX.getRow(2).getCell((short) 4);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getBrand() ));
                //H3
                cell2 = sheetX.getRow(2).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getMachineType().getName() ));

                //C4
                cell2 = sheetX.getRow(3).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getHeadNum().toString()));
                //E4
                cell2 = sheetX.getRow(3).getCell((short) 4);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getHeadNum().toString()));
                //H4
                cell2 = sheetX.getRow(3).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getxDistance()));

                //H5
                cell2 = sheetX.getRow(4).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getyDistance()));

                //D6
                cell2 = sheetX.getRow(5).getCell((short) 3);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getSpecialTowelColor()));
                //F6
                cell2 = sheetX.getRow(5).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getSpecialTowelDaxle()));
                //H6
                cell2 = sheetX.getRow(5).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getSpecialTowelHaxle()));
                //K6
                cell2 = sheetX.getRow(5).getCell((short) 10);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getSpecialTowelMotor()));

                //D7
                cell2 = sheetX.getRow(6).getCell((short) 3);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getSpecialTapingHead()));
                //H7
                cell2 = sheetX.getRow(6).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getSpecialTowelNeedle()));

                //C8
                cell2 = sheetX.getRow(7).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getElectricPc()));
                //D8
                cell2 = sheetX.getRow(7).getCell((short) 3);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getCountry()));
                //F8
                cell2 = sheetX.getRow(7).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getElectricMotor()));
                //I8
                cell2 = sheetX.getRow(7).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getElectricMotorXy()));

                //C9
                cell2 = sheetX.getRow(8).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getElectricTrim()));
                //F9
                cell2 = sheetX.getRow(8).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getElectricPower()));
                //I9
                cell2 = sheetX.getRow(8).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getElectricSwitch()));

                //C10
                cell2 = sheetX.getRow(9).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getElectricOil()));

                //C11
                cell2 = sheetX.getRow(10).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getAxleSplit()));
                //F11
                cell2 = sheetX.getRow(10).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getAxlePanel()));
                //i11
                cell2 = sheetX.getRow(10).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getAxleNeedle()));

                //C12
                cell2 = sheetX.getRow(11).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getAxleRail()));
                //f12
                cell2 = sheetX.getRow(11).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getAxleDownCheck()));
                //i12
                cell2 = sheetX.getRow(11).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getAxleHook()));

                //C13
                cell2 = sheetX.getRow(12).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getAxleJump()));
                //F13
                cell2 = sheetX.getRow(12).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getAxleUpperThread()));

                //C14
                cell2 = sheetX.getRow(13).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getAxleAddition()));

                //C15
                cell2 = sheetX.getRow(14).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getFrameworkColor()));
                //f15
                cell2 = sheetX.getRow(14).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getFrameworkPlaten()));
                //i15
                cell2 = sheetX.getRow(14).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getFrameworkRing()));

                //C16
                cell2 = sheetX.getRow(15).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getFrameworkBracket()));
                //f16
                cell2 = sheetX.getRow(15).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getFrameworkStop()));
                //i16
                cell2 = sheetX.getRow(15).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getFrameworkLight()));

                //C17
                cell2 = sheetX.getRow(16).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getDriverType()));
                //f17
                cell2 = sheetX.getRow(16).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getDriverMethod()));
                //i17 数据库缺少字段？

                //C18
                cell2 = sheetX.getRow(17).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getDriverHorizonNum().toString()));
                //C19
                cell2 = sheetX.getRow(18).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getOrderDetail().getDriverVerticalNum().toString()));
                //C20
                cell2 = sheetX.getRow(19).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString( machineOrderDetail.getPackageMethod()));

                //C21
                cell2 = sheetX.getRow(20).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(contract.getContractShipDate().toString()));
                cell2.setCellValue(new HSSFRichTextString(dateTimeString));
                //F21 订机数量
                cell2 = sheetX.getRow(20).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getMachineNum().toString()));

                //i21 单价
                cell2 = sheetX.getRow(20).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getMachinePrice()));

                //C22
                cell2 = sheetX.getRow(21).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getPlanShipDate().toString()));
                cell2.setCellValue(new HSSFRichTextString(dateTimeString));
                //i22 总价
                cell2 = sheetX.getRow(21).getCell((short) 8);
                Integer machineOrderSum = Integer.parseInt(machineOrderDetail.getMachinePrice())*machineOrderDetail.getMachineNum();
                cell2.setCellValue(new HSSFRichTextString(machineOrderSum.toString()));

                //A23
                cell2 = sheetX.getRow(22).getCell((short) 0);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getMark()));

                //C30
                cell2 = sheetX.getRow(29).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(contract.getSellman()));

                //需求单审核信息，来自 order_sign
                orderSignList = orderSignService.getOrderSignListByOrderId(machineOrderIdList.get(i));

                if(orderSignList.size() > 0) {
                    //取最后一次的签核，后续看是否需要根据时间来取最新
                    orderSign = orderSignList.get(orderSignList.size()-1);

                    signContentItemList = JSON.parseArray(orderSign.getSignContent(), SignContentItem.class);

                    for ( int j=0;j<signContentItemList.size();j++ ) {
                        signContentItem = signContentItemList.get(j);
                        if(Constant.TECH_DEP_STEP.equals( signContentItem.getNumber())){
                            //技术部签核顺位
                            OderSignCommentByTechDep = signContentItem.getComment();
                            orderSignUserByTechDep = signContentItem.getUser();
                            if(signContentItem.getDate() != null){
                                orderSignDateByTechDep = formatter2.format(signContentItem.getDate());
                            }
                        } else if(Constant.PMC_STEP.equals( signContentItem.getNumber()) ) {
                            //PMC
                            OrderSignCommentByPMC = signContentItem.getComment();
                            orderSignUserByPMC = signContentItem.getUser();
                            if(signContentItem.getDate() !=null){
                                orderSignDateByPMC = formatter2.format(signContentItem.getDate());
                            }
                        } else if(Constant.FINANCIAL_DEP_DEPOSIT_STEP.equals( signContentItem.getNumber()) ) {
                            //财务 定金审核
                            OrderSignCommentByFinancialDeposit = signContentItem.getComment();
                            orderSignUserByFinancialDeposit = signContentItem.getUser();
                            if (signContentItem.getDate() != null) {
                                orderSignDateByFinancialDeposit = formatter2.format(signContentItem.getDate());
                            }
                        }
                    }

                    //C32 技术部评审
                    cell2 = sheetX.getRow(31).getCell((short) 2);
                    cell2.setCellValue(new HSSFRichTextString(orderSignUserByTechDep));
                    //D32
                    cell2 = sheetX.getRow(31).getCell((short) 3);
                    cell2.setCellValue(new HSSFRichTextString(orderSignDateByTechDep));
                    //G32
                    cell2 = sheetX.getRow(31).getCell((short) 6);
                    cell2.setCellValue(new HSSFRichTextString(OderSignCommentByTechDep));

                    //C33
                    cell2 = sheetX.getRow(32).getCell((short)2);
                    cell2.setCellValue(new HSSFRichTextString(orderSignUserByPMC));
                    //D33
                    cell2 = sheetX.getRow(32).getCell((short) 3);
                    cell2.setCellValue(new HSSFRichTextString(orderSignDateByPMC));
                    //G33
                    cell2 = sheetX.getRow(32).getCell((short) 6);
                    cell2.setCellValue(new HSSFRichTextString(OrderSignCommentByPMC));

                    //C35
                    cell2 = sheetX.getRow(34).getCell((short)2);
                    cell2.setCellValue(new HSSFRichTextString(orderSignUserByFinancialDeposit));
                    //D35
                    cell2 = sheetX.getRow(34).getCell((short) 3);
                    cell2.setCellValue(new HSSFRichTextString(orderSignDateByFinancialDeposit));
                    //G35
                    cell2 = sheetX.getRow(34).getCell((short)6);
                    cell2.setCellValue(new HSSFRichTextString(OrderSignCommentByFinancialDeposit));
                }
            }
  
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