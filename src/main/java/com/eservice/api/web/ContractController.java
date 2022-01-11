package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultCode;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract.ContractDetail;
import com.eservice.api.model.contract.Equipment;
import com.eservice.api.model.contract.MachineOrderWrapper;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.design_dep_info.DesignDepInfo;
import com.eservice.api.model.design_dep_info.DesignDepInfoDetail;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_change_record.OrderChangeRecord;
import com.eservice.api.model.order_detail.OrderDetail;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.model.order_split_record.OrderSplitRecord;
import com.eservice.api.model.user.User;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.common.Utils;
import com.eservice.api.service.impl.*;
import com.eservice.api.service.mqtt.MqttMessageHelper;
import com.eservice.api.service.mqtt.ServerToClientMsg;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class Description: xxx
 *
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
    private UserServiceImpl userService;
    @Resource
    private ContactFormServiceImpl contactFormService;
    @Resource
    private CommonService commonService;
    @Resource
    private OrderChangeRecordServiceImpl orderChangeRecordService;
    @Resource
    private OrderSplitRecordServiceImpl orderSplitRecordService;
    @Resource
    private MachineServiceImpl machineService;
    @Resource
    private RoleServiceImpl roleService;
    @Resource
    private MqttMessageHelper mqttMessageHelper;

    @Resource
    DesignDepInfoServiceImpl designDepInfoService;
    @Value("${contract_excel_output_dir}")
    private String contractOutputDir;
    private Logger logger = Logger.getLogger(ContractController.class);
    private boolean isDebug = false;

    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public Result add(String contract, String contractSign, String requisitionForms) {
        if (contract == null || "".equals(contract)) {
            return ResultGenerator.genFailResult("合同信息为空！");
        }
        if (contractSign == null || "".equals(contractSign)) {
            return ResultGenerator.genFailResult("合同审核初始化信息为空！");
        }
        if (requisitionForms == null || "".equals(requisitionForms)) {
            return ResultGenerator.genFailResult("订单信息为空！");
        }
        Contract contract1 = JSONObject.parseObject(contract, Contract.class);
        if (contract1 == null) {
            return ResultGenerator.genFailResult("Contract对象JSON解析失败！");
        }

//        Condition condition = new Condition(Contract.class);
//        condition.createCriteria().andCondition("contract_num = ", contract1.getContractNum());
//        List<Contract> list = contractService.findByCondition(condition);

        List<Contract> list = contractService.isContractExist( contract1.getContractNum() );
        if (list.size() != 0) {
           logger.error("合同编号已存在");
           return ResultGenerator.genFailResult("合同编号已存在！请换一个编号");
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
        if (machineOrderWapperList != null) {
            //是内贸还是外贸的订单
            String salesDepartment = null;
            User machineOrderCreator = null;
            for (int i = 0; i < machineOrderWapperList.size(); i++) {
                OrderDetail temp = machineOrderWapperList.get(i).getOrderDetail();
                MachineOrder orderTemp = machineOrderWapperList.get(i).getMachineOrder();
                orderDetailService.saveAndGetID(temp);
                orderTemp.setOrderDetailId(temp.getId());
                orderTemp.setContractId(contractId);
                orderTemp.setStatus(Constant.ORDER_INITIAL);

                Condition condition2 = new Condition(Contract.class);
                condition2.createCriteria().andCondition("order_num = ", orderTemp.getOrderNum())
                        .andCondition("valid = ", 1);
                List<MachineOrder> orderList = machineOrderService.findByCondition(condition2);
                if (orderList.size() != 0) {
                    logger.error("订单号已存在！请换一个");
                    return ResultGenerator.genFailResult("订单号已存在！请换一个");
                }

                machineOrderService.saveAndGetID(orderTemp);

                //初始化需求单审核记录
                OrderSign orderSignData = machineOrderWapperList.get(i).getOrderSign();
                OrderSign orderSign = new OrderSign();
                orderSign.setSignContent(orderSignData.getSignContent());
                orderSign.setOrderId(orderTemp.getId());
                orderSign.setCreateTime(new Date());
                orderSign.setCurrentStep("");
                //是内贸还是外贸 --> 因为要查询订单是哪个部门，所以要细化到外贸一部还是二部
                if(salesDepartment == null) {
                    machineOrderCreator = userService.findById(orderTemp.getCreateUserId());
                    if (machineOrderCreator.getMarketGroupName().equals(Constant.STR_DEPARTMENT_DOMESTIC)) {
                        salesDepartment = Constant.STR_DEPARTMENT_DOMESTIC;
                    } else if (machineOrderCreator.getMarketGroupName().equals(Constant.STR_DEPARTMENT_FOREIGN_1)) {
                        salesDepartment = Constant.STR_DEPARTMENT_FOREIGN_1;
                    }  else if (machineOrderCreator.getMarketGroupName().equals(Constant.STR_DEPARTMENT_FOREIGN_2)) {
                        salesDepartment = Constant.STR_DEPARTMENT_FOREIGN_2;
                    } else if(machineOrderCreator.getRoleId() == Constant.ROLE_ID_FOREIGN_DIRECTOR){
                        /**
                         * 外贸总监的MarketGroupName部门为空, 外贸总监录的订单，也算为一部的。
                         * 外贸经理的MarketGroupName部门为外贸二部
                         */
                        salesDepartment = Constant.STR_DEPARTMENT_FOREIGN_1;
                    }
                    orderSign.setSalesDepartment(salesDepartment);
                }
                orderSignService.saveAndGetID(orderSign);
                //第一个轮到签核的人也发消息推送  --> 此时为未提交状态，不需要发推送

            }
        } else {
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultGenerator.genFailResult("需求单为空！");
        }
        //返回ID给前端，前端新增合同时不关闭页面。
        return ResultGenerator.genSuccessResult(contract1.getId());
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        contractService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/updateValid")
    public Result updateValid(@RequestParam Integer id) {
        if (id < 1) {
            return ResultGenerator.genFailResult("合同编号不正确，请检查！");
        }
        Contract contractObj = new Contract();
        contractObj.setId(id);
        contractObj.setIsValid((Constant.ValidEnum.INVALID.getValue()).toString());
        contractObj.setUpdateTime(new Date());
        contractService.update(contractObj);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public Result update(String contract, String requisitionForms) {
        Contract contract1 = JSONObject.parseObject(contract, Contract.class);
        List<MachineOrderWrapper> machineOrderWapperlist = JSONObject.parseArray(requisitionForms, MachineOrderWrapper.class);
        //先获取当前合同的所有订单
        List<MachineOrderDetail> originalOrderList = machineOrderService.selectOrder(null, contract1.getId(), null, null, null,
                null, null, null, null, null, null,
                null,null,null,null, false);
        ///删除该合同下，不在本次保存范围内的需求单
        for (MachineOrderDetail item : originalOrderList) {
            boolean exist = false;
            /**
             * web有时会发来有问题的requisitionForms
             */
            if(machineOrderWapperlist == null){
                logger.error("machineOrderWapperlist is null, return;");
                return ResultGenerator.genFailResult("machineOrderWapperlist is null, nothing updated");
            }
            if(requisitionForms.isEmpty() || requisitionForms==null){
                logger.error("requisitionForms is empty/null, return;");
                return ResultGenerator.genFailResult("requisitionForms is empty/null, nothing updated");
            }

            for (MachineOrderWrapper wapperItem : machineOrderWapperlist) { //null
                if (item.getId().equals(wapperItem.getMachineOrder().getId())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                //删除需求单审核记录
                OrderSign orderSign = orderSignService.findBy("orderId", item.getId());
                if (orderSign != null) {
                    orderSignService.deleteById(orderSign.getId());
                }
                //删除需求单  ....
                /**
                 *  同时要删除对应的设计单.
                 *  比如，在更新订单时，改了订单号，旧的订单会被删除，此时如果没有删除对应设计单，会无法删除订单。
                 *  后来改为订单审核完成才生成设计单，上述情形应该不存在了。
                 */
                deleteDDIbyOrder(item);
                machineOrderService.deleteById(item.getId());
                //删除detail
                orderDetailService.deleteById(item.getOrderDetailId());
            }
        }

        for (MachineOrderWrapper item : machineOrderWapperlist) {

            //是内贸还是外贸的订单
            String salesDepartment = null;
            User machineOrderCreator = null;
            MachineOrder orderTemp = item.getMachineOrder();
            OrderChangeRecord changeRecord = item.getOrderChangeRecord();
            if (orderTemp.getId() != null && orderTemp.getId() != 0) {
                //更新，只对initial和reject状态的需求单就是更新，其他状态的需求单不做更新
                OrderDetail temp = item.getOrderDetail();
                if (orderTemp.getStatus().equals(Constant.ORDER_REJECTED)) {
                    orderTemp.setStatus(Constant.ORDER_INITIAL);
                }
                if (orderTemp.getStatus().equals(Constant.ORDER_INITIAL)) {
                    orderDetailService.update(temp);
                    machineOrderService.update(orderTemp);
                    commonService.syncMachineOrderStatusInDesignDepInfo(orderTemp);
                    // 在改单之后，在重新提交之前，允许修改改单原因，即：改单原因不仅仅在改单时允许修改，在上述情况下也允许修改。
                    if( null != changeRecord ) {
                        orderChangeRecordService.update(changeRecord);
                    }
                }
            } else {
                //新增
                OrderDetail temp = item.getOrderDetail();
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
                //是内贸还是外贸一部，二部
                if(salesDepartment == null) {
                    machineOrderCreator = userService.findById(orderTemp.getCreateUserId());
                    if (machineOrderCreator.getMarketGroupName().equals(Constant.STR_DEPARTMENT_DOMESTIC)) {
                        salesDepartment = Constant.STR_DEPARTMENT_DOMESTIC;
                    } else if (machineOrderCreator.getMarketGroupName().equals(Constant.STR_DEPARTMENT_FOREIGN_1)) {
                        salesDepartment = Constant.STR_DEPARTMENT_FOREIGN_1;
                    }  else if (machineOrderCreator.getMarketGroupName().equals(Constant.STR_DEPARTMENT_FOREIGN_2)) {
                        salesDepartment = Constant.STR_DEPARTMENT_FOREIGN_2;
                    } else if(machineOrderCreator.getRoleId() == Constant.ROLE_ID_FOREIGN_DIRECTOR){
                        /**
                         * 外贸总监的MarketGroupName部门为空, 外贸总监录的订单，也算为一部的。
                         * 外贸经理的MarketGroupName部门为外贸二部
                         */
                        salesDepartment = Constant.STR_DEPARTMENT_FOREIGN_1;
                    }
                    orderSign.setSalesDepartment(salesDepartment);
                }
                orderSignService.save(orderSign);

            }
        }
        //前端只要操作了“保存”，合同的状态回到“CONTRACT_INITIAL”状态
        contract1.setStatus(Constant.CONTRACT_INITIAL);
        contract1.setUpdateTime(new Date());
        contractService.update(contract1);

        //检查需求单中的销售员和合同中销售员是否一致，如果不一致则更新需求单中的销售员
        for (MachineOrderWrapper item : machineOrderWapperlist) {
            if(!item.getMachineOrder().getSellman().equals(contract1.getSellman())) {
                MachineOrder order = new MachineOrder();
                order.setId(item.getMachineOrder().getId());
                //只更新销售员
                order.setSellman(contract1.getSellman());
                machineOrderService.update(order);
            }
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除了该订单 对应的设计单
     * @param machineOrderDetail
     */
    private void deleteDDIbyOrder(MachineOrderDetail machineOrderDetail){
        List<DesignDepInfoDetail> designDepInfoDetailList = designDepInfoService.selectDesignDepInfo(
                machineOrderDetail.getOrderNum(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        if(designDepInfoDetailList !=null && designDepInfoDetailList.size() !=0) {
            logger.info("syncMachineOrderStatusInDesignDepInfo,designDepInfoDetailList.size():" + designDepInfoDetailList.size());
            designDepInfoDetailList.get(0).setOrderSignStatus(machineOrderDetail.getStatus());
            logger.info("designDepInfoDetailList.get(0).getOrderNum()" + designDepInfoDetailList.get(0).getOrderNum());
            logger.info("designDepInfoDetailList.get(0).getId()" + designDepInfoDetailList.get(0).getId());
            logger.info("designDepInfoDetailList.get(0).getOrderId()" + designDepInfoDetailList.get(0).getOrderId());
            designDepInfoService.deleteById(designDepInfoDetailList.get(0).getId());
            logger.info("删除了该订单" + machineOrderDetail.getOrderNum() + " 对应的设计单");
        } else {
            logger.warn("删除该订单 " + machineOrderDetail.getOrderNum() + "  对应的设计单，根据该订单号找不到设计单，设计单还没生成，或是没有设计单之前的旧订单");
        }


    }
    /**
     * 联系单审核通过才允许改单，
     * 改单后新生成的订单不用再审批，因为联系单已经审核过了。
     */
    @PostMapping("/changeOrder")
    @Transactional(rollbackFor = Exception.class)
    public Result changeOrder(String contract, String contractSign, String requisitionForms) {
        logger.info("changeOrder=======:");
        logger.info(requisitionForms);
        if (contract == null || "".equals(contract)) {
            return ResultGenerator.genFailResult("合同信息为空！");
        }
        if (contractSign == null || "".equals(contractSign)) {
            return ResultGenerator.genFailResult("合同审核初始化信息为空！");
        }
        if (requisitionForms == null || "".equals(requisitionForms)) {
            return ResultGenerator.genFailResult("订单信息为空！");
        }
        Contract contract1 = JSONObject.parseObject(contract, Contract.class);
        if (contract1 == null) {
            return ResultGenerator.genFailResult("Contract对象JSON解析失败！");
        }


        //更改合同的状态为“改单”
        contract1.setStatus(Constant.CONTRACT_CHANGED);
        contract1.setUpdateTime(new Date());
        contractService.update(contract1);
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
        //返回新增的改单的ID号给前端，前端新增改单时不关闭页面。
        int newMachineOrderId = 0;
        List<MachineOrderWrapper> machineOrderWrapperList = JSONObject.parseArray(requisitionForms, MachineOrderWrapper.class);
        for (MachineOrderWrapper orderItem : machineOrderWrapperList) {
            MachineOrder machineOrder = orderItem.getMachineOrder();
            //检查时候存在有效的需求单,如果需求单中存在ID,则表示其已经在数据库中存在;修复同一个合同中多次改单（未提交审核）出现重复的需求单的问题
            if (machineOrder.getStatus().equals(Constant.ORDER_INITIAL) && machineOrder.getOriginalOrderId() != 0
                    && (machineOrder.getId() == null || machineOrder.getId() == 0)) {
                //插入新增改单项的detail
                OrderDetail temp = orderItem.getOrderDetail();
                orderDetailService.saveAndGetID(temp);
                machineOrder.setOrderDetailId(temp.getId());
                machineOrder.setContractId(contract1.getId());

                //改单的前提是原订单已审核完成，联系单已经审核通过，所以不需要再重新审核，
                machineOrder.setStatus(Constant.ORDER_CHANGE_FINISHED);

                machineOrder.setCreateTime(new Date());
                machineOrderService.saveAndGetID(machineOrder);
                ///设计单里的状态 也要改 -->改的地方多，统一放在定时器里去更新状态-->废弃，因为订单可能很多

                newMachineOrderId = machineOrder.getId();

                /**
                 * 为了让改单后的新订单也能看到签核的时间等，改单新生成的订单，也有对应的审核记录，
                 * 审核内容和步骤，来自于旧的审核记录。（和原订单的一样的签核记录）
                 */
                //初始化需求单审核记录
                OrderSign orderSignData = orderItem.getOrderSign();
                OrderSign orderSign = new OrderSign();

                orderSign.setSignContent(orderSignData.getSignContent());
                orderSign.setOrderId(machineOrder.getId());
                orderSign.setCreateTime(orderSignData.getCreateTime());
                orderSign.setCurrentStep(orderSignData.getCurrentStep());
                orderSign.setUpdateTime(orderSignData.getUpdateTime());
                orderSignService.save(orderSign);

                //改单记录(插入或者修改)
                OrderChangeRecord changeRecord = orderItem.getOrderChangeRecord();
                if (changeRecord.getId() == null) {
                    changeRecord.setChangeTime(new Date());
                    orderChangeRecordService.save(changeRecord);
                } else {
                    changeRecord.setChangeTime(new Date());
                    orderChangeRecordService.update(changeRecord);
                }
            }
        }

        for (MachineOrderWrapper orderItem : machineOrderWrapperList) {
            MachineOrder machineOrder = orderItem.getMachineOrder();
            //设置被改单的需求单状态(machine_order/order_sign)
            /**
             * 一个合同下，多个订单，多个改单
             * 前面已经被改好的改单，状态为 ORDER_CHANGED， 改好后的订单 状态为 ORDER_CHANGE_FINISHED
             * 改单刚刚开始时（还在改单中），状态为 ORDER_CHANGE_ING， 由前端传过来，
             * 这种情况表示正在改单，要把状态设置为 ORDER_CHANGED，其他的照旧处理
             * 之所以分 ORDER_CHANGE_ING、ORDER_CHANGED，是因为为了修复 多个订单都改单的情况下，对应的机器数量会处理不对
             * 一个订单改单-不改机器数量：OK
             * 一个订单改单-新增机器数量：OK
             * 一个订单改单-减少机器数量：OK
             *
             * 2个订单都改单-不改机器数量：OK
             * 2个订单都改单-新增机器数量：ok
             * 2个订单都改单-减少机器数量：OK
             *
             */
            if (machineOrder.getStatus().equals(Constant.ORDER_CHANGE_ING)) {
                //更新了被改的需求单为“改单”，持久化至数据库
                machineOrder.setUpdateTime(new Date());
                machineOrder.setStatus(Constant.ORDER_CHANGED);
                machineOrderService.update(machineOrder);
                //获取被改单对应机器，设置改单状态(machine)
                Condition tempCondition = new Condition(Machine.class);
                tempCondition.createCriteria().andCondition("order_id = ", machineOrder.getId());
                List<Machine> machineList = machineService.findByCondition(tempCondition); ///被改订单的
                //寻找对应新生成的需求单，比较机器数
                MachineOrder newOrder = null;
                for (MachineOrderWrapper wrapper : machineOrderWrapperList) {
                    if (wrapper.getMachineOrder().getOriginalOrderId().equals(machineOrder.getId())) {
                        newOrder = wrapper.getMachineOrder();
                        break;
                    }
                }
                if (newOrder != null) {
                    //改单后，机器数量不变或者变少了，需要把这部分机器设置为取消（无论是否已生产）
                    if(newOrder.getMachineNum()<= machineList.size()) {
                        logger.info("改单后，机器数量不变或者变少了, 新订单的机器数： " + newOrder.getMachineNum());
                        //step1 按新订单的机器数量，把 被改订单的机器 挂到新生成的订单
                        for (int m=0; m<newOrder.getMachineNum(); m++) {
                            Machine machine = machineList.get(m);
                            ///初始化、取消状态，直接将机器的上的需求单号直接绑定到新需求单。 其他状态的机器则改为 改单
                            logger.info("新生成的订单号" + newOrder.getOrderNum() + "的 【实际的】 机器编号： " + machine.getNameplate() + "，状态： " + machine.getStatus());
                            if (machine.getStatus().equals(Constant.MACHINE_INITIAL) || machine.getStatus().equals(Constant.MACHINE_CANCELED)) {
                            } else {
                                machine.setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CHANGED)));
                                ///有改单状态的机器，通知全部安装组长
                                ServerToClientMsg msg = new ServerToClientMsg();
                                msg.setOrderNum(newOrder.getOrderNum());
                                msg.setNameplate(machine.getNameplate());
                                msg.setType(ServerToClientMsg.MsgType.ORDER_CHANGE);
                                mqttMessageHelper.sendToClient(Constant.S2C_MACHINE_STATUS_CHANGE, JSON.toJSONString(msg));
                            }
                            machine.setOrderId(newOrder.getId());
                            machine.setUpdateTime(new Date());
                            machineService.update(machine);
                        }
                        ////step2把被改订单里剩下的机器设置为取消
                        /**
                         * 因为在改单时不确定哪些机子在生产了，所以管理员根据需要可以在"生产管理"页面，根据实际情况调整机器铭牌号。
                         */
                        tempCondition.createCriteria().andCondition("order_id = ", machineOrder.getId());
                        List<Machine> machineToBeCancelledList = machineService.findByCondition(tempCondition);
                        for (Machine machine : machineToBeCancelledList) { //如果机器数量不变，这里为0
                            machine.setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CANCELED)));
                            machine.setUpdateTime(new Date());
                            logger.info("把剩下的机器设置为取消 " + machine.getNameplate());
                            machineService.update(machine);
                        }
                    } else {
                        /**
                         * 改单后，机器数量增加了，需要新增机器 ,原先：多出部分机器在审核完成以后自动添加
                         * --> 2020-0409：有联系单后不再需要审核订单了，所以需要在此生成机器。
                         */
                        logger.info("改单前，原订单机器数量： " + machineList.size());
                        logger.info("改单后，机器数量增加到了： " + newOrder.getMachineNum());
                        ////step1  把 被改订单的机器 全部之间挂到新生成的订单，即旧订单就没机器了。
                        for (int n=0; n<machineList.size(); n++) {
                            Machine machine = machineList.get(n);
                            ///初始化、取消状态，直接将机器的上的需求单号直接绑定到新需求单。 其他状态的机器则改为 改单 （为啥这样？全部改成改单不行吗）
                            logger.info("新生成的订单号" + newOrder.getOrderNum() + "的 【实际的】 机器编号： " + machine.getNameplate() + "，状态： " + machine.getStatus());
                            if (machine.getStatus().equals(Constant.MACHINE_INITIAL) || machine.getStatus().equals(Constant.MACHINE_CANCELED)) {
                            } else {
                                machine.setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CHANGED)));
                                ///有改单状态的机器，通知全部安装组长
                                ServerToClientMsg msg = new ServerToClientMsg();
                                msg.setOrderNum(newOrder.getOrderNum());
                                msg.setNameplate(machine.getNameplate());
                                msg.setType(ServerToClientMsg.MsgType.ORDER_CHANGE);
                                mqttMessageHelper.sendToClient(Constant.S2C_MACHINE_STATUS_CHANGE, JSON.toJSONString(msg));
                            }
                            machine.setOrderId(newOrder.getId());
                            machine.setUpdateTime(new Date());
                            machineService.update(machine);
                        }
                        ////step2 再补足 新增的机器
                        int haveToCreate = newOrder.getMachineNum() - machineList.size();
                        logger.info("订单" + newOrder.getOrderNum() + "，需要新增机器台数：" + haveToCreate);
                        for (int c = 0; c < haveToCreate; c++) {
                            Machine machine = new Machine();
                            machine.setMachineStrId(Utils.createMachineBasicId() + c);
                            machine.setOrderId(newOrder.getId());
                            machine.setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_INITIAL)));
                            machine.setMachineType(newOrder.getMachineType());
                            machine.setCreateTime(new Date());

                            if (newOrder.getAllUrgent() == null || !newOrder.getAllUrgent()) {
                                machine.setIsUrgent(false);
                            } else {
                                machine.setIsUrgent(true);
                            }
                            machineService.save(machine);
                            logger.info("have created machine's id: " + machine.getId());
                        }
                    }

                    /* 20180323精简了算法，对于被改的需求单，除了初始化和取消状态的机器保持状态不变，其他机器都设置为该到为状态
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
                            if (machine.getStatus().equals(Constant.MACHINE_CONFIGURED)
                                    ||machine.getStatus().equals(Constant.MACHINE_PLANING)
                                    || machine.getStatus().equals( Constant.MACHINE_INSTALLING)
                                    || machine.getStatus().equals(Constant.MACHINE_INSTALLED)
                                    || machine.getStatus().equals(Constant.MACHINE_SPLITED)) {
                                ///查找已配置、计划中、生产中、被拆单、生产完成的机器
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
                            ///对于删除状态的机器不做处理
//                            else {
//                                originalOtherMachine.get(i).setStatus(Byte.parseByte(String.valueOf(Constant.MACHINE_CANCELED)));
//                                machineService.update(originalInitialMachine.get(i));
//                            }
                        }
                    }
                    */
                } else {
                    ///在同一个合同中没有找到新的需求单,抛出异常
                    throw new RuntimeException();
                }
            }
        }

        return ResultGenerator.genSuccessResult(newMachineOrderId);

    }

    /**
     * 为所有已改单，已拆单，补充签核记录（和原订单的一样的签核记录）
     * 用于：报表里只显示已改后的新订单，要显示终审日期），
     * 因为之前联系单签核之后，新改新拆的订单的审核数据在报表里也要用到了。
     *
     * 这个接口只要在部署前执行一次，因为以后改单拆单的签核，已经在代码里有做了（和原订单的一样的签核记录）
     */
    @PostMapping("/syncOrdersignForChangedAndSplitted")
    public Result syncOrdersignForChangedAndSplitted(){
        int count = 0;
        List<OrderSign> allOrderSignList = orderSignService.findAll();
        for(int i=0; i<allOrderSignList.size(); i++){
            if(allOrderSignList.get(i).getSignContent().equals("[]") || allOrderSignList.get(i).getUpdateTime()== null ){
                MachineOrder machineOrder = machineOrderService.findById(allOrderSignList.get(i).getOrderId());
                if(machineOrder.getStatus() == Constant.ORDER_SPLIT_FINISHED || machineOrder.getStatus() == Constant.ORDER_CHANGE_FINISHED) {
                    logger.info("已改单/拆单 订单 " + machineOrder.getOrderNum() + "，需要补充签核记录");
                    List<OrderSign> theOriginalOrderSignList = orderSignService.getOrderSignListByOrderId(machineOrder.getOriginalOrderId());
                    if(theOriginalOrderSignList.size() !=1) {
                        logger.error("异常，找到的原订单的签核记录不是1个");
                        return ResultGenerator.genFailResult("异常，找到的原订单的签核记录不是1个");
                    }

                    allOrderSignList.get(i).setCurrentStep(theOriginalOrderSignList.get(0).getCurrentStep());
                    allOrderSignList.get(i).setSignContent(theOriginalOrderSignList.get(0).getSignContent());

                    //这个时间，考虑改为来自于联系单的终审时间 ，目前用旧的审核
//                    contactFormService.
                    allOrderSignList.get(i).setUpdateTime(theOriginalOrderSignList.get(0).getUpdateTime());
                    orderSignService.update(allOrderSignList.get(i));
                    count++;
                    logger.info(count + ": 已改单/拆单 订单 " + machineOrder.getOrderNum() + "更新了 签核记录");
                }
            }
        }
        return ResultGenerator.genSuccessResult("一共更新了 " + count + " 个签核");
    }


    @PostMapping("/splitOrder")
    @Transactional(rollbackFor = Exception.class)
    public Result splitOrder(String contract, String contractSign, String requisitionForms, String splitMachines) {
        if (contract == null || "".equals(contract)) {
            return ResultGenerator.genFailResult("合同信息为空！");
        }
        if (contractSign == null || "".equals(contractSign)) {
            return ResultGenerator.genFailResult("合同审核初始化信息为空！");
        }
        if (requisitionForms == null || "".equals(requisitionForms)) {
            return ResultGenerator.genFailResult("订单信息为空！");
        }
        if (splitMachines == null || "".equals(splitMachines)) {
            return ResultGenerator.genFailResult("拆单机器信息为空！");
        }

        Contract contractObj = JSONObject.parseObject(contract, Contract.class);
        if (contractObj == null || contractSign == null || requisitionForms == null || splitMachines == null) {
            return ResultGenerator.genFailResult("JSON解析失败！");
        }

        //更改合同的状态为“拆单”
        contractObj.setStatus(Constant.CONTRACT_SPLITED);
        contractObj.setUpdateTime(new Date());
        contractService.update(contractObj);
        Integer contractId = contractObj.getId();
        ///插入新的contract审核记录
        ContractSign contractSignObj = new ContractSign();
        contractSignObj.setContractId(contractId);
        contractSignObj.setCreateTime(new Date());
        contractSignObj.setSignContent(contractSign);
        ///插入空值
        contractSignObj.setCurrentStep("");
        contractSignService.save(contractSignObj);

        //新增的拆单处理
        //返回新增的拆单的ID号给前端，前端新增改单时不关闭页面。
        int newMachineOrderId = 0;
        List<MachineOrderWrapper> machineOrderWrapperList = JSONObject.parseArray(requisitionForms, MachineOrderWrapper.class);
        List<Machine> splitMachineList = JSONObject.parseArray(splitMachines, Machine.class);

        for (MachineOrderWrapper orderItem : machineOrderWrapperList) {
            MachineOrder machineOrder = orderItem.getMachineOrder();
            if (machineOrder.getId() == null && machineOrder.getOriginalOrderId() != 0) {
                //插入新增改单项的detail
                OrderDetail temp = orderItem.getOrderDetail();
                orderDetailService.saveAndGetID(temp);
                machineOrder.setOrderDetailId(temp.getId());
                machineOrder.setContractId(contractObj.getId());
                //改单的前提是原订单已审核完成，联系单已经审核通过，所以不需要再重新审核，
//                machineOrder.setStatus(Constant.ORDER_INITIAL);
                machineOrder.setStatus(Constant.ORDER_SPLIT_FINISHED);
                machineOrder.setCreateTime(new Date());

                /**
                 * 订单 不允许同名
                 * 带下划线的字段，不能用findBy(fieldName,....)
                 */
                try {
                    Class cl = Class.forName("com.eservice.api.model.machine_order.MachineOrder");
                    Field fieldOrderNum = cl.getDeclaredField("orderNum");

                    MachineOrder mo = null;
                    mo = machineOrderService.findBy(fieldOrderNum.getName(), machineOrder.getOrderNum());
                    if (mo != null) {
                        logger.error( " splitOrder 该 订单号已存在，请确认是否重名 " +  machineOrder.getOrderNum() );
                        return ResultGenerator.genFailResult(machineOrder.getOrderNum() + " 该订单号已存在，请确认是否重名 ");
                    }
                } catch (ClassNotFoundException e) {
                    logger.error( "splitOrder fail: " +e.getMessage());
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    logger.error( "splitOrder fail: " +e.getMessage());
                    e.printStackTrace();
                }

                machineOrderService.saveAndGetID(machineOrder);
                newMachineOrderId = machineOrder.getId();

                /**
                 * 为了让 拆单后的新订单也能看到签核的时间等， 新生成的订单，也有对应的审核记录，
                 * 审核内容和步骤，来自于旧的审核记录。
                 */
                //初始化需求单审核记录
                OrderSign orderSignData = orderItem.getOrderSign();
                OrderSign orderSign = new OrderSign();

                orderSign.setSignContent(orderSignData.getSignContent());
                orderSign.setOrderId(machineOrder.getId());
                orderSign.setCreateTime(orderSignData.getCreateTime());
                orderSign.setCurrentStep(orderSignData.getCurrentStep());
                orderSign.setUpdateTime(orderSignData.getUpdateTime());
                orderSignService.save(orderSign);

                //被拆分出来的机器绑定到新的需求单
                for (Machine splitMachine : splitMachineList) {
                    splitMachine.setOrderId(machineOrder.getId());
                    splitMachine.setStatus(Constant.MACHINE_SPLITED);
                    ///MQTT 有拆单状态的机器，通知全部安装组长
                    ServerToClientMsg msg = new ServerToClientMsg();
                    msg.setOrderNum(machineOrder.getOrderNum());
                    msg.setNameplate(splitMachine.getNameplate());
                    msg.setType(ServerToClientMsg.MsgType.ORDER_SPLIT);
                    mqttMessageHelper.sendToClient(Constant.S2C_MACHINE_STATUS_CHANGE, JSON.toJSONString(msg));
                    splitMachine.setUpdateTime(new Date());
                    //正常数据时是不需要设置
//                    if(splitMachine.getIsUrgent().equals("")){
//                        splitMachine.setIsUrgent(null);
//                    }
                    machineService.update(splitMachine);
                }

                //拆单记录(插入或者修改)
                OrderSplitRecord splitRecord = orderItem.getOrderSplitRecord();
                if (splitRecord.getId() == null) {
                    splitRecord.setSplitTime(new Date());
                    splitRecord.setOrderId(machineOrder.getId());
                    orderSplitRecordService.save(splitRecord);
                } else {
                    splitRecord.setSplitTime(new Date());
                    orderSplitRecordService.update(splitRecord);
                }
            }
        }

        //处于拆单状态的需求单，更新状态成“ORDER_SPLIT”
        for (MachineOrderWrapper orderItem : machineOrderWrapperList) {
            MachineOrder machineOrder = orderItem.getMachineOrder();
            //TODO:同一个合同中其他为“ORDER_SPLIT”状态的需求单也会被更新，需要完善
            if (machineOrder.getStatus().equals(Constant.ORDER_SPLITED)) {
                machineOrder.setUpdateTime(new Date());
                machineOrderService.update(machineOrder);
            }
        }

        return ResultGenerator.genSuccessResult(newMachineOrderId);
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

    /**
     *
     * @param contractNum
     * @param status
     * @param sellman
     * @param recordUser    录单人
     * @param roleName      当前审批阶段的角色名称
     * @param marketGroupName   订单属于哪个市场部（仅仅分内贸部，外贸一部，外贸二部，没有“外贸部”）
     * 查询时， contract.market_group_name的内容有3钟：”内贸部","外贸一部", "外贸二部"
     *                              如果是内贸经理、内贸销售员：只看内贸订单，查询用词"内贸部“
     *                              如果是外贸经理：看外贸一部、二部订单，查询用词"外贸“ 两个字，后台会匹配“外贸一部”和“外贸二部”
     *                              如果是外贸一部销售员：只看外贸一部订单，查询用词"外贸一部“ 4个字
     *                              如果是外贸二部销售员：只看外贸二部订单，查询用词"外贸二部“ 4个字
     *                              如果是外贸总监：看外贸一部、二部订单，查询用词"外贸“ 两个字，后台会匹配“外贸一部”和“外贸二部”
     * @param query_start_time
     * @param query_finish_time
     * @param userDomesticTradeZoneListStr 登录者的内贸分区，可以多个，非内贸经理时，为空。
     * @param is_fuzzy
     * @return
     */
    @PostMapping("/selectContracts")
    public Result selectContracts(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "0") Integer size,
                                  String contractNum,
                                  Integer status,
                                  String sellman,
                                  String recordUser,
                                  String roleName,
                                  String marketGroupName,
                                  String query_start_time,
                                  String query_finish_time,
                                  String userDomesticTradeZoneListStr,
                                  @RequestParam(defaultValue = "true") Boolean is_fuzzy) {
        PageHelper.startPage(page, size);

//        外贸经理：看外贸一部、二部订单，查询用词"外贸“ 两个字，后台已经模糊查询，匹配“外贸一部”和“外贸二部”
//        外贸总监：看外贸一部、二部订单，查询用词"外贸“ 两个字，后台一i就模糊查询，匹配“外贸一部”和“外贸二部”
//        if (marketGroupName != null && !marketGroupName.isEmpty()) {
//            if (marketGroupName.equals(Constant.STR_DEPARTMENT_FOREIGN_FUZZY)) {
//                marketGroupName = Constant.STR_DEPARTMENT_FOREIGN_FUZZY;
//            }
//        }
        List<ContractDetail> list = contractService.selectContracts(contractNum, status, sellman, recordUser, roleName, marketGroupName, query_start_time, query_finish_time, userDomesticTradeZoneListStr, is_fuzzy);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);

    }

    @PostMapping("/startSign")
    @Transactional(rollbackFor = Exception.class)
    public Result startSign(@RequestParam Integer contractId) {
        if (contractId == null) {
            ResultGenerator.genFailResult("合同ID为空！");
        } else {
            ContractSign contractSign = contractSignService.detailByContractId(String.valueOf(contractId));
            if (contractSign == null) {
                return ResultGenerator.genFailResult("根据合同号获取合同签核信息失败！");
            } else {
                //更新合同状态为“CONTRACT_CHECKING”
                Contract contract = contractService.findById(contractId);
                if (contract == null) {
                    return ResultGenerator.genFailResult("合同编号ID无效");
                } else {
                    contract.setStatus(Constant.CONTRACT_CHECKING);
                    contract.setUpdateTime(new Date());
                    contractService.update(contract);
                }

                //设置该合同下的需求单状态，如果处于“ORDER_INITIAL”状态，则设置为“ORDER_CHECKING”
                Condition tempCondition = new Condition(MachineOrder.class);
                tempCondition.createCriteria().andCondition("contract_id = ", contractId)
                        .andCondition("status = ", Constant.ORDER_INITIAL)
                        .andCondition("valid = ", 1);

                List<MachineOrder> orderList = machineOrderService.findByCondition(tempCondition);
                for (MachineOrder orderItem : orderList) {
                    if (orderItem.getStatus().equals(Constant.ORDER_INITIAL)) {
                        orderItem.setStatus(Constant.ORDER_CHECKING);
                        machineOrderService.update(orderItem);
                        commonService.syncMachineOrderStatusInDesignDepInfo(orderItem);
                        Condition signCondition = new Condition(OrderSign.class);
                        signCondition.createCriteria().andCondition("order_id = ", orderItem.getId());
                        List<OrderSign> orderSignList = orderSignService.findByCondition(signCondition);
                        //无签核记录
                        if (orderSignList.size() == 0) {
                            throw new RuntimeException();
                        } else {
                            OrderSign sign = orderSignList.get(orderSignList.size() - 1);
                            List<SignContentItem> orderSignContentList = JSON.parseArray(sign.getSignContent(), SignContentItem.class);
                            sign.setCurrentStep(roleService.findById(orderSignContentList.get(0).getRoleId()).getRoleName());
                            orderSignService.update(sign);

                            /**
                             * 发起签核时 也给第一个签核人推送
                             */
                            commonService.pushMachineOrderMsgToAftersale(sign,
                                    contract,
                                    orderItem,
                                    false,
                                    Constant.STR_MSG_PUSH_IS_TURN_TO_SIGN);

                        }
                    }
                }

                //更新签核记录
//                contractSign.setUpdateTime(new Date());
//                String currentStep = commonService.getCurrentSignStep(contractId);
//                if (currentStep == null) {
//                    return ResultGenerator.genFailResult("获取当前签核steps失败！");
//                }
//                contractSign.setCurrentStep(currentStep);
//                //设置完状态后更新签核记录
//                contractSignService.update(contractSign);
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据 contract_id，创建EXCEL表格，“合同评审单”+“客户需求单” 等sheet。
     * 具体内容来自 contract, contract_sign,machine_order,order_detail
     * Update: 总经理，销售，财务之外的用户，生成的excel里不显示金额信息.
     *
     * @param contractId
     * @return
     */
    @PostMapping("/buildContractExcel")
    public Result buildContractExcel(@RequestParam Integer contractId, @RequestParam String account) {
        InputStream fs = null;
        POIFSFileSystem pfs = null;
        HSSFWorkbook wb = null;
        FileOutputStream out = null;
        String downloadPath = "";
        /*
        返回给docker外部下载
         */
        String downloadPathForNginx = "";

        int focusLine = 0;
        /**
         * 某订单总价 = 机器价格*台数 + 装置价格*台数 - 优惠总价
         */
//        Integer totalPriceOfOrder = 0;
        Double totalPriceOfOrder = 0.0;

        /**
         * 某订单的机器总价
         */
//        Integer machineOrderSum = 0;
        Double machineOrderSum = 0.0;
        /**
         * 合同总价 = 各订单总价之和
         */
//        Integer totalPriceOfContract = 0;
        Double totalPriceOfContract = 0.0;

        //只有总经理，销售，财务等用户，生成的excel里才显示金额信息. '6','7','9','14','15'
        Boolean displayPrice = commonService.isDisplayPrice(account);
        try {
            ClassPathResource resource = new ClassPathResource("empty_contract.xls");
            fs = resource.getInputStream();
            pfs = new POIFSFileSystem(fs);
            wb = new HSSFWorkbook(pfs);
            HSSFFont fontSlim = wb.createFont();
            fontSlim.setBold(false);
            HSSFCellStyle cellStyleSlim= wb.createCellStyle();
            cellStyleSlim.setFont(fontSlim);
            cellStyleSlim.setBorderBottom(BorderStyle.THIN);
            cellStyleSlim.setBorderLeft(BorderStyle.THIN);
            cellStyleSlim.setBorderTop(BorderStyle.THIN);
            cellStyleSlim.setBorderRight(BorderStyle.THIN);
            cellStyleSlim.setAlignment(HorizontalAlignment.CENTER);
            cellStyleSlim.setVerticalAlignment(VerticalAlignment.CENTER);

            Contract contract = contractService.findById(contractId);
            if (contract == null) {
                return ResultGenerator.genFailResult("contractID not exist!");
            }

            //一个合同可能对应多个需求单
            List<Integer> machineOrderIdList = new ArrayList<Integer>();
            List<Integer> validMachineOrderIdList = new ArrayList<Integer>();
            MachineOrder mo;
            Condition tempCondition = new Condition(MachineOrder.class);
            tempCondition.createCriteria().andCondition("contract_id = ", contractId)
                    .andCondition("valid = ", 1);
            List<MachineOrder> validOrderList = machineOrderService.findByCondition(tempCondition);
            for (int i = 0; i < validOrderList.size(); i++) {
                mo = validOrderList.get(i);
                // (已改的单，是废弃的单，不用再显示在excel里,已拆的单,因为是有效的，所以保留着)
                if (mo.getValid().intValue() == 1) {
                    machineOrderIdList.add(mo.getId());
                    if(mo.getStatus().intValue() != Constant.ORDER_CHANGED.intValue()) {
                        validMachineOrderIdList.add(mo.getId());
                    }
                }
            }
            MachineOrderDetail machineOrderDetail;
            //需求单签核,一个需求单对应0个或多个签核
            List<OrderSign> orderSignList;

            //读取了模板内所有sheet1内容
            HSSFSheet sheet1 = wb.getSheetAt(0);
            //在相应的单元格进行赋值(A2)
            HSSFCell cell = sheet1.getRow(1).getCell((short) 0);
            cell.setCellValue(new HSSFRichTextString("合 同 号：" + contract.getContractNum()));
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

            //N个需求单，插入N行（机器）,不包括已改单的需求单
            Integer validMachineOrderCount = validMachineOrderIdList.size();
            insertRow(wb, sheet1, 5, validMachineOrderCount);
            if (isDebug) {
                System.out.println("======== insert Rows validMachineOrderCount: " + validMachineOrderCount);
            }

            /**
             * 记录各个订单的装置数量
             */
            int[] equipmentNumArr = new int[validMachineOrderCount];

            for (int i = 0; i < validMachineOrderCount; i++) {
                machineOrderDetail = machineOrderService.getOrderAllDetail(validMachineOrderIdList.get(i));

                //计算在合同sheet用到装置价格信息，
                JSONArray jsonArray = JSON.parseArray(machineOrderDetail.getEquipment());
                Integer equipmentCount = 0;

                if (null != jsonArray) {
                    //该需求单的X个装置，插入X + 3行 (3是优惠/居间/需求单小计)
                    equipmentCount = jsonArray.size();
                    insertRow(wb, sheet1, 5 , equipmentCount+3 );
                    equipmentNumArr[i] = equipmentCount;
                    if (isDebug) {
                        System.out.println("======== insert Rows: " + (equipmentCount + 3) + "for equipments");
                    }
                }
            }

            String machineInfo = "";
            for (int i = 0; i < validMachineOrderCount; i++) {
                totalPriceOfOrder = 0.0;
                machineOrderSum = 0.0;
                machineOrderDetail = machineOrderService.getOrderAllDetail(validMachineOrderIdList.get(i));
                focusLine = 5 + i + getLinesSum(equipmentNumArr, i ) ;

                //A5,,...订单号
                cell = sheet1.getRow(focusLine).getCell((short) 0);
                cell.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderNum()));
                //B5,...机型详细信息：机型/针数/头数/头距/x行程/y行程/剪线方式/电脑
                cell = sheet1.getRow(focusLine).getCell((short) 1);
                machineInfo = machineOrderDetail.getMachineType().getName() + "/"
                        + machineOrderDetail.getNeedleNum() + "/"
                        + machineOrderDetail.getHeadNum() + "/"
                        + machineOrderDetail.getHeadDistance() + "/"
                        + machineOrderDetail.getxDistance() + "/"
                        + machineOrderDetail.getyDistance() + "/"
                        + machineOrderDetail.getOrderDetail().getElectricTrim() + "/"
                        + machineOrderDetail.getOrderDetail().getElectricPc();
                cell.setCellValue(new HSSFRichTextString(machineInfo));

                //C5,,...数量
                cell = sheet1.getRow(focusLine).getCell((short) 2);
                cell.setCellValue(new HSSFRichTextString(machineOrderDetail.getMachineNum().toString()));

                //D5,,...单价
                cell = sheet1.getRow(focusLine).getCell((short) 3);
                if (displayPrice) {
                    cell.setCellValue(new HSSFRichTextString(machineOrderDetail.getMachinePrice()));
                } else {
                    cell.setCellValue(new HSSFRichTextString("/"));
                }

                //E5,,..机器总价
                cell = sheet1.getRow(focusLine).getCell((short) 4);

                machineOrderSum = Double.parseDouble(machineOrderDetail.getMachinePrice())* machineOrderDetail.getMachineNum();

                totalPriceOfOrder += machineOrderSum;
                if (displayPrice) {
                    cell.setCellValue(machineOrderSum);
                } else {
                    cell.setCellValue(new HSSFRichTextString("/"));
                }
                if (isDebug) {
                    System.out.println("===order: " + machineOrderDetail.getOrderNum() + " inserted machine @" + focusLine);
                }

                /**
                 * 该订单的各个装置
                 */
                JSONArray jsonArray = JSON.parseArray(machineOrderDetail.getEquipment());
                Integer equipmentCount = 0;
//                int orderEquipmentTotal = 0;
                double orderEquipmentTotal = 0;
                if (null != jsonArray) {

                    focusLine = 6 + i + getLinesSum(equipmentNumArr, i );
                    equipmentCount = jsonArray.size();

                    for (int j = 0; j < equipmentCount; j++) {
                        Equipment eq = JSON.parseObject((String) jsonArray.get(j).toString(), Equipment.class);

                        /**
                         * 前端传进来异常数据时的处理 
                         * 如果某数据为空 就都不写了。
                         */
                        if(eq.getName() == null) {
                            logger.error("异常数据，装置名称为空");
                            break;
                        }
                        if(eq.getNumber() == null) {
                            logger.error("异常数据，装置数量为空");
                            break;
                        }
                        if(eq.getPrice() == null) {
                            logger.error("异常数据，装置价格为空");
                            break;
                        }
                        if(eq.getType() == null) {
                            logger.error("异常数据，装置类型为空");
                            break;
                        }

                        cell = sheet1.getRow(focusLine + j).getCell((short) 1);
                        cell.setCellValue(new HSSFRichTextString(eq.getName() + (eq.getType() != null && !"".equals(eq.getType()) ? "(" + eq.getType() + ")" : "")));
                        cell.setCellStyle(cellStyleSlim);

                        /**
                         * 这里的数量是：每台机器的装置数*机器台数
                         */
                        cell = sheet1.getRow(focusLine + j).getCell((short) 2);
                        cell.setCellValue(eq.getNumber() * machineOrderDetail.getMachineNum());
                        cell.setCellStyle(cellStyleSlim);

                        cell = sheet1.getRow(focusLine + j).getCell((short) 3);
                        if (displayPrice) {
                            cell.setCellValue(new HSSFRichTextString(eq.getPrice().toString()));
                        } else {
                            cell.setCellValue(new HSSFRichTextString("/"));
                        }
                        cell.setCellStyle(cellStyleSlim);

                        /**
                         * 订单内该种装置的总价
                         */
                        cell = sheet1.getRow(focusLine + j).getCell((short) 4);
//                        int eqSum = eq.getNumber() * eq.getPrice() * machineOrderDetail.getMachineNum();
                        Double eqSum = eq.getNumber() * eq.getPrice() * machineOrderDetail.getMachineNum();
                        orderEquipmentTotal = orderEquipmentTotal + eqSum;
                        totalPriceOfOrder += eqSum;
                        if (displayPrice) {
//                            cell.setCellValue(new HSSFRichTextString((Integer.toString(eqSum))));
                            cell.setCellValue(new HSSFRichTextString((eqSum.toString())));
                        } else {
                            cell.setCellValue(new HSSFRichTextString("/"));
                        }

                    }
                    focusLine += equipmentCount;

                }

                /**
                 * 该订单的优惠
                 */
                if (isDebug) {
                    System.out.println("========order: " + machineOrderDetail.getOrderNum() + " inserted 优惠 @" + focusLine);
                }
                cell = sheet1.getRow(focusLine).getCell((short) 0);
                cell.setCellValue(new HSSFRichTextString("优惠金额:"));

                cell = sheet1.getRow(focusLine).getCell((short) 4);
                /**
                 * 该订单的 总优惠 = 优惠金额/台 * 台数 +  优惠金额 (用于抹零等)
                 * 既有 优惠金额/台，又有优惠金额 。类似折上折的意思
                 */
//                Integer sumOfDiscounts =Integer.parseInt(machineOrderDetail.getDiscounts()) * machineOrderDetail.getMachineNum() + Integer.parseInt(machineOrderDetail.getOrderTotalDiscounts());
                Double sumOfDiscounts =Double.parseDouble(machineOrderDetail.getDiscounts()) * machineOrderDetail.getMachineNum() + Double.parseDouble(machineOrderDetail.getOrderTotalDiscounts());
                if (displayPrice) {
                    cell.setCellValue(new HSSFRichTextString(sumOfDiscounts.toString()));
                } else {
                    cell.setCellValue(new HSSFRichTextString("/"));
                }
                totalPriceOfOrder -= sumOfDiscounts;
                if (isDebug) {
                    System.out.println("========order: " + machineOrderDetail.getOrderNum() + "  old totalPriceOfContract:" + totalPriceOfContract);
                }
                totalPriceOfContract += totalPriceOfOrder;
                if (isDebug) {
                    System.out.println("========order: " + machineOrderDetail.getOrderNum() + " now totalPriceOfContract:" + totalPriceOfContract + "by added:  " + totalPriceOfOrder);
                }

                focusLine++;
                cell = sheet1.getRow(focusLine).getCell((short) 0);
                cell.setCellValue(new HSSFRichTextString("居间费用"));

                //居间费用的台数
                cell = sheet1.getRow(focusLine).getCell((short) 2);
                if (displayPrice) {
                    cell.setCellValue( machineOrderDetail.getMachineNum() );
                } else {
                    cell.setCellValue(new HSSFRichTextString("/"));
                }

                //居间费用/台 (单价)
                cell = sheet1.getRow(focusLine).getCell((short) 3);
                if (displayPrice) {
                    cell.setCellValue(new HSSFRichTextString(machineOrderDetail.getIntermediaryPrice()));
                } else {
                    cell.setCellValue(new HSSFRichTextString("/"));
                }

                // 居间费用总计,不计入订单总价
                cell = sheet1.getRow(focusLine).getCell((short) 4);
                if (displayPrice) {
//                    Integer sumOfIntermediary =Integer.parseInt(machineOrderDetail.getIntermediaryPrice()) * machineOrderDetail.getMachineNum();
                    Double sumOfIntermediary =Double.parseDouble(machineOrderDetail.getIntermediaryPrice()) * machineOrderDetail.getMachineNum();
                    cell.setCellValue(new HSSFRichTextString(sumOfIntermediary.toString()));
                } else {
                    cell.setCellValue(new HSSFRichTextString("/"));
                }
                if (isDebug) {
                    System.out.println("========order: " + machineOrderDetail.getOrderNum() + " inserted 居间 @" + focusLine);
                }

                /**
                 * 需求单小计
                 */
                focusLine++;
                cell = sheet1.getRow(focusLine).getCell((short) 0);
                cell.setCellValue(new HSSFRichTextString("小 计"));

                cell = sheet1.getRow(focusLine).getCell((short) 2);
                cell.setCellValue(new HSSFRichTextString("/"));

                cell = sheet1.getRow(focusLine).getCell((short) 3);
                cell.setCellValue(new HSSFRichTextString("/"));

                //需求单小计
                cell = sheet1.getRow(focusLine).getCell((short) 4);
                if (displayPrice) {
//                    Integer orderTotalPrice = machineOrderSum + orderEquipmentTotal - sumOfDiscounts;
                    Double orderTotalPrice = machineOrderSum + orderEquipmentTotal - sumOfDiscounts;
                    cell.setCellValue(new HSSFRichTextString(orderTotalPrice.toString()));
                } else {
                    cell.setCellValue(new HSSFRichTextString("/"));
                }
                if (isDebug) {
                    System.out.println("========order: " + machineOrderDetail.getOrderNum() + " inserted 小计 @" + focusLine);
                }

            }//end of order

            //删除需求单行的空白的多余一行
            sheet1.shiftRows(focusLine , sheet1.getLastRowNum(),-1);

            // 合同总计
            focusLine ++;
            if (isDebug) {
                System.out.println("===============合同总价 @" + focusLine + " line, focusLine is: " + focusLine);
            }
            cell = sheet1.getRow(focusLine++).getCell((short) 4);
            if (displayPrice) {
                cell.setCellValue(new HSSFRichTextString(totalPriceOfContract.toString()));
            } else {
                cell.setCellValue(new HSSFRichTextString("/"));
            }

            // 付款方式
            cell = sheet1.getRow(focusLine++).getCell((short) 1);
            if (displayPrice) {
                cell.setCellValue(new HSSFRichTextString(contract.getPayMethod()));
            } else {
                cell.setCellValue(new HSSFRichTextString("/"));
            }

            // 币种
            cell = sheet1.getRow(focusLine++).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(contract.getCurrencyType()));

            // 合同交货日期
            String dateTimeString = formatter.format(contract.getContractShipDate());
            cell = sheet1.getRow(focusLine++).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(dateTimeString));

            // 备注
            cell = sheet1.getRow(focusLine++).getCell((short) 0);
            if (displayPrice) {
                cell.setCellValue(new HSSFRichTextString(contract.getMark()));
            } else {
                cell.setCellValue(new HSSFRichTextString("/"));
            }

            // 销售员
            focusLine = focusLine + 6;
            cell = sheet1.getRow(focusLine++).getCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(contract.getSellman()));

            //一个合同对应多个签核 TODO:多个签核时如何选择，contractSignService.detailByContractId 可能要改。
            ContractSign contractSign;
            // 合同审核信息，来自 contract_sign
            contractSign = contractSignService.detailByContractId(contractId.toString());
            SignContentItem signContentItem;
            List<SignContentItem> signContentItemList = JSON.parseArray(contractSign.getSignContent(), SignContentItem.class);
            OrderSign orderSign = null;

            /**
             *  根据签核内容 signContentItemList，动态填入表格。
             *  signType为 “合同签核”的，都按顺序填入表格
             */
            //合同的N个签核，插入N行
            Integer contractSignCount = signContentItemList.size();
            insertRow(wb, sheet1, focusLine, contractSignCount);
            for (int k = 0; k < contractSignCount; k++) {
                /**
                 * 合同签核的： 角色（部门）/人/时间/意见
                 */
                //1.签核角色（部门）
                int roleId = signContentItemList.get(k).getRoleId();
                //根据roleId返回角色（部门）
                String roleName = roleService.findById(roleId).getRoleName();
                cell = sheet1.getRow(focusLine).getCell((short) 0);
                cell.setCellValue(new HSSFRichTextString(roleName));
                //2.签核人
                cell = sheet1.getRow(focusLine).getCell((short) 1);
                cell.setCellValue(new HSSFRichTextString(signContentItemList.get(k).getUser()));
                //3.签核时间
                cell = sheet1.getRow(focusLine).getCell((short) 2);
                if (null != signContentItemList.get(k).getDate()) {
                    cell.setCellValue(new HSSFRichTextString(formatter2.format(signContentItemList.get(k).getDate())));
                }
                cell = sheet1.getRow(focusLine).getCell((short) 3);
                cell.setCellValue(new HSSFRichTextString("意见"));
                //4.签核意见
                cell = sheet1.getRow(focusLine++).getCell((short) 4);
                if(displayPrice) {
                    cell.setCellValue(new HSSFRichTextString(signContentItemList.get(k).getComment()));
                } else {
                    /**
                     * 财务(财务会计，财务经理，成本核算员)的意见，仅特定人员可见
                     */
                    if (// signContentItemList.get(k).getRoleId().equals(7) //销售部经理
                        //    || signContentItemList.get(k).getRoleId().equals(9)     //销售员
                            signContentItemList.get(k).getRoleId().equals(13)    //成本核算员
                            || signContentItemList.get(k).getRoleId().equals(14)    //财务经理
                            || signContentItemList.get(k).getRoleId().equals(15) )  //财务会计

                        cell.setCellValue(new HSSFRichTextString("/"));
                }

            }
            //删除最后多余一行
            sheet1.shiftRows(focusLine + 1,
                    sheet1.getLastRowNum(),
                    -1);

            Integer machineOrderCount = machineOrderIdList.size();
            /**
             * 需求单
             */
            //根据实际需求单数量，动态复制生成新的sheet;
            for (int i = 0; i < machineOrderCount - 1; i++) {
                // clone已经包含copy+paste
                wb.cloneSheet(1);
            }
            //调整sheet位置
            Integer sheetCount = wb.getNumberOfSheets();
            wb.setSheetOrder("Sheet3", sheetCount - 1);

            //sheet2，sheet3...,第1,2,...个需求单
            for (int i = 0; i < machineOrderCount; i++) {
                totalPriceOfOrder = 0.0;
                machineOrderSum = 0.0;
                machineOrderDetail = machineOrderService.getOrderAllDetail(machineOrderIdList.get(i));
                //把sheet名称改为订单的编号
                wb.setSheetName(i + 1, machineOrderDetail.getOrderNum().replaceAll("/", "-"));

                HSSFSheet sheetX = wb.getSheetAt(1 + i);
                //在相应的单元格进行赋值
                //B2
                HSSFCell cell2 = sheetX.getRow(1).getCell((short) 1);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getSellman()));
                //D2
                cell2 = sheetX.getRow(1).getCell((short) 3);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getMaintainPerson()));
                //F2
                cell2 = sheetX.getRow(1).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderNum()));
                // I2
                cell2 = sheetX.getRow(1).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString(contract.getContractNum()));

                //C3
                cell2 = sheetX.getRow(2).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getCustomer()));
                //E3
                cell2 = sheetX.getRow(2).getCell((short) 4);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getBrand()));
                //g3
                cell2 = sheetX.getRow(2).getCell((short) 6);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getMachineType().getName()));
                //I3 机头款式
                cell2 = sheetX.getRow(2).getCell((short) 8);
                if(machineOrderDetail.getMachineHeadStyle() != null) { ///新旧订单兼容。旧订单没有这项
                    cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getMachineHeadStyle()));
                }
                //C4
                cell2 = sheetX.getRow(3).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getNeedleNum()
                        + "/" + machineOrderDetail.getHeadNum().toString()));
                //E4
                cell2 = sheetX.getRow(3).getCell((short) 4);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getHeadDistance().toString()));
                //H4
                cell2 = sheetX.getRow(3).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getxDistance()));

                //H5
                cell2 = sheetX.getRow(4).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getyDistance()));

                //D6 毛巾机头
                cell2 = sheetX.getRow(5).getCell((short) 3);
                if(machineOrderDetail.getOrderDetail().getSpecialTowelHead() != null) {
                    cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getSpecialTowelHead()));
                }
                //F6 毛巾色数
                cell2 = sheetX.getRow(5).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getSpecialTowelColor()));
                //H6 毛巾机针
                cell2 = sheetX.getRow(5).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getSpecialTowelNeedle()));
                //K6 主电机
                cell2 = sheetX.getRow(5).getCell((short) 10);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getSpecialTowelMotor()));

                //D7
                cell2 = sheetX.getRow(6).getCell((short) 3);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getSpecialTapingHead()));
                //H7

                //C8 电控型号（电脑）
                cell2 = sheetX.getRow(7).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getElectricPc()));
                //D8
                cell2 = sheetX.getRow(7).getCell((short) 3);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getElectricLanguage()));
                //F8
                cell2 = sheetX.getRow(7).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getElectricMotor()));
                //I8
                cell2 = sheetX.getRow(7).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getElectricMotorXy()));

                //C9
                cell2 = sheetX.getRow(8).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getElectricTrim()));
                //F9
                cell2 = sheetX.getRow(8).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getElectricPower()));
                //I9 改为了 换色方式
                cell2 = sheetX.getRow(8).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getColorChangeMode()));

                //C10
                cell2 = sheetX.getRow(9).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getElectricOil()));

                //C11
                cell2 = sheetX.getRow(10).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxleSplit()));
                //F11
                cell2 = sheetX.getRow(10).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxlePanel()));
                //i11
                cell2 = sheetX.getRow(10).getCell((short) 8);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxleNeedle()));
                cell2 = sheetX.getRow(10).getCell((short) 10);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxleNeedleType()));

                //C12
                cell2 = sheetX.getRow(11).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxleHook()));
                //f12 取消
//                cell2 = sheetX.getRow(11).getCell((short) 5);
//                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxleDownCheck()));
                //i12 取消
//                cell2 = sheetX.getRow(11).getCell((short) 8);
//                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxleHook()));

                //C13
                cell2 = sheetX.getRow(12).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxleJump()));
                //F13
                cell2 = sheetX.getRow(12).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxleUpperThread()));

                //C14
                cell2 = sheetX.getRow(13).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getAxleAddition()));

                //C15
                cell2 = sheetX.getRow(14).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getFrameworkColor()));
                //f15 机脚类型
                cell2 = sheetX.getRow(14).getCell((short) 5);
                if(machineOrderDetail.getOrderDetail().getFrameworkMachineFootType() !=null) {
                    cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getFrameworkMachineFootType()));
                }
                //G15
                //h15 台板支撑
                cell2 = sheetX.getRow(14).getCell((short) 7);
                if(machineOrderDetail.getOrderDetail().getFrameworkPlatenSupport() != null) {
                    cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getFrameworkPlatenSupport()));
                }
                //k15
                cell2 = sheetX.getRow(14).getCell((short) 10);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getFrameworkRing()));

                //C16 电脑托架
                cell2 = sheetX.getRow(15).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getFrameworkBracket()));

                //f16
                cell2 = sheetX.getRow(15).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getFrameworkStop()));
                //H16 台板颜色
                cell2 = sheetX.getRow(15).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getFrameworkPlatenColor()));

                //K16
                cell2 = sheetX.getRow(15).getCell((short) 10);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getFrameworkPoleHeight()));
                //i16 取消
//                cell2 = sheetX.getRow(15).getCell((short) 8);
//                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getFrameworkLight()));

                //C17 X驱动类型
                cell2 = sheetX.getRow(16).getCell((short) 2);
                if(machineOrderDetail.getOrderDetail().getDriverXType() != null) {
                    cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getDriverXType()));
                }
                //f17 y驱动类型
                cell2 = sheetX.getRow(16).getCell((short) 5);
                if(machineOrderDetail.getOrderDetail().getDriverYType() != null) {
                    cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getDriverYType()));
                }
                //H17 框架类型 (原先的驱动方式)
                cell2 = sheetX.getRow(16).getCell((short) 7);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getDriverMethod()));

                //F18
                cell2 = sheetX.getRow(17).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getDriverReelHole()));
                //F19
                cell2 = sheetX.getRow(18).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getDriverReel()));

                //C18
                cell2 = sheetX.getRow(17).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getDriverHorizonNum().toString()));
                //C19
                cell2 = sheetX.getRow(18).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderDetail().getDriverVerticalNum().toString()));
                //b20 包装方式：
                cell2 = sheetX.getRow(19).getCell((short) 1);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getPackageMethod()));
                //d20 线架拆除：
                cell2 = sheetX.getRow(19).getCell((short) 3);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getWrapStandRemove()));

                //F20
                cell2 = sheetX.getRow(19).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getPackageMark()));

                //C21
                cell2 = sheetX.getRow(20).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getWrapMachine()));

                //F21
                cell2 = sheetX.getRow(20).getCell((short) 5);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getWrapMachineChange()));

                //C22，23 ... N 装置名称
                String str = machineOrderDetail.getEquipment();
                JSONArray jsonArray = JSON.parseArray(str);
                Integer equipmentCount = 0;
                if (null != jsonArray) {
                    //该需求单的N个装置，插入N行
                    equipmentCount = jsonArray.size();
                    insertRow2(wb, sheetX, 22, equipmentCount - 1);
                    if (isDebug) {
                        System.out.println("========order: " + machineOrderDetail.getOrderNum() + " inserted " + equipmentCount + " line");
                    }
                    for (int j = 0; j < equipmentCount; j++) {
                        Equipment eq = JSON.parseObject((String) jsonArray.get(j).toString(), Equipment.class);

                        /**
                         * 前端传进来异常数据时的处理
                         * 如果某数据为空 就都不写了。
                         */
                        if(eq.getName() == null) {
                            logger.error("异常数据，装置名称为空");
                            break;
                        }
                        if(eq.getNumber() == null) {
                            logger.error("异常数据，装置数量为空");
                            break;
                        }
                        if(eq.getPrice() == null) {
                            logger.error("异常数据，装置价格为空");
                            break;
                        }
                        if(eq.getType() == null) {
                            logger.error("异常数据，装置类型为空");
                            break;
                        }

                        cell2 = sheetX.getRow(22 + j).getCell((short) 0);
                        cell2.setCellValue(new HSSFRichTextString(Integer.toString(j + 1)));

                        cell2 = sheetX.getRow(22 + j).getCell((short) 1);
                        cell2.setCellValue(new HSSFRichTextString(eq.getName() + (eq.getType() != null && !"".equals(eq.getType()) ? "(" + eq.getType() + ")" : "")));

                        cell2 = sheetX.getRow(22 + j).getCell((short) 2);
                        /**
                         * 这里的数量是：每台机器的装置数*机器台数
                         */
                        cell2.setCellValue(eq.getNumber()* machineOrderDetail.getMachineNum());

                        //单价
                        cell2 = sheetX.getRow(22 + j).getCell((short) 3);
                        if (displayPrice) {
                            cell2.setCellValue(new HSSFRichTextString(eq.getPrice().toString()));
                        } else {
                            cell2.setCellValue(new HSSFRichTextString("/"));
                        }
                        //总价
                        cell2 = sheetX.getRow(22 + j).getCell((short) 4);
//                        int eqSum = eq.getNumber() * eq.getPrice() * machineOrderDetail.getMachineNum();
                        Double eqSum = eq.getNumber() * eq.getPrice() * machineOrderDetail.getMachineNum();
                        totalPriceOfOrder += eqSum;
                        if (displayPrice) {
                            cell2.setCellValue(new HSSFRichTextString(eqSum.toString()));
                        } else {
                            cell2.setCellValue(new HSSFRichTextString("/"));
                        }
                    }

                } else {
                    if (isDebug) {
                        System.out.println("========order: " + machineOrderDetail.getOrderNum() + " inserted 000 line");
                    }
                }//装置end

                /**
                 *  空表格里是斜线，不用显示
                 */
//                //优惠价格/台
//                cell2 = sheetX.getRow(22 + equipmentCount).getCell((short) 3);
//                if (displayPrice) {
//                    cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getDiscounts()));
//                } else {
//                    cell2.setCellValue(new HSSFRichTextString("/"));
//                }

                /**
                 * 优惠总计
                 * 该订单的 总优惠 = 优惠金额/台 * 台数 +  优惠金额 (用于抹零等)
                 * 既有 优惠金额/台，又有优惠金额 。类似折上折的意思
                 */
                cell2 = sheetX.getRow(22 + equipmentCount).getCell((short) 4);
                if (displayPrice) {
                    //Integer sumOfDiscounts =Integer.parseInt(machineOrderDetail.getDiscounts()) * machineOrderDetail.getMachineNum() + Integer.parseInt(machineOrderDetail.getOrderTotalDiscounts());
                    /**
                     * 用 xx + yy 的形式展示具体的值，如果存在折上折时，可以清楚看到具体情况：  xx(即优惠金额/台 * 台数) + yy（即优惠金额）
                     */
//                    Integer sumOfDiscounts =Integer.parseInt(machineOrderDetail.getDiscounts()) * machineOrderDetail.getMachineNum();
                    Double sumOfDiscounts =Double.parseDouble(machineOrderDetail.getDiscounts()) * machineOrderDetail.getMachineNum();
                    cell2.setCellValue(new HSSFRichTextString(sumOfDiscounts.toString()) + " + " + machineOrderDetail.getOrderTotalDiscounts());
                    totalPriceOfOrder -= (sumOfDiscounts + Double.parseDouble( machineOrderDetail.getOrderTotalDiscounts()));
                } else {
                    cell2.setCellValue(new HSSFRichTextString("/"));
                }
                //居间费用的台数
                cell2 = sheetX.getRow(23 + equipmentCount).getCell((short) 2);
                if (displayPrice) {
                    cell2.setCellValue( machineOrderDetail.getMachineNum() );
                } else {
                    cell2.setCellValue(new HSSFRichTextString("/"));
                }
                //居间费用/台
                cell2 = sheetX.getRow(23 + equipmentCount).getCell((short) 3);
                if (displayPrice) {
                    cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getIntermediaryPrice()));
                }else {
                    cell2.setCellValue(new HSSFRichTextString("/"));
                }

                //居间费用总计  不计入订单总价
                cell2 = sheetX.getRow(23 + equipmentCount).getCell((short) 4);
                if (displayPrice) {
                    Double sumOfIntermediary =Double.parseDouble(machineOrderDetail.getIntermediaryPrice()) * machineOrderDetail.getMachineNum();
                    cell2.setCellValue(new HSSFRichTextString(sumOfIntermediary.toString()));
                } else {
                    cell2.setCellValue(new HSSFRichTextString("/"));
                }

                // 订机数量
                cell2 = sheetX.getRow(24 + equipmentCount).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getMachineNum().toString()));
                // 机器单价
                cell2 = sheetX.getRow(24 + equipmentCount).getCell((short) 3);
                if (displayPrice) {
                    cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getMachinePrice()));
                } else {
                    cell2.setCellValue(new HSSFRichTextString("/"));
                }
                // 机器总价
                machineOrderSum = Double.parseDouble(machineOrderDetail.getMachinePrice()) * machineOrderDetail.getMachineNum();
                cell2 = sheetX.getRow(24 + equipmentCount).getCell((short) 4);
                if (displayPrice) {
                    cell2.setCellValue(machineOrderSum);
                } else {
                    cell2.setCellValue(new HSSFRichTextString("/"));
                }

                // 需求单总价
                totalPriceOfOrder += machineOrderSum;
                cell2 = sheetX.getRow(25 + equipmentCount).getCell((short) 4);
                if (displayPrice) {
                    cell2.setCellValue(totalPriceOfOrder);
                } else {
                    cell2.setCellValue(new HSSFRichTextString("/"));
                }

                // 合同的交货日期
                cell2 = sheetX.getRow(26 + equipmentCount).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(formatter2.format(contract.getContractShipDate())));

                // 计划发货日期
                cell2 = sheetX.getRow(27 + equipmentCount).getCell((short) 2);
                cell2.setCellValue(new HSSFRichTextString(formatter2.format(machineOrderDetail.getPlanShipDate())));

                // 备注
                cell2 = sheetX.getRow(28 + equipmentCount).getCell((short) 0);
                cell2.setCellValue(new HSSFRichTextString(machineOrderDetail.getMark()));

                //判断改单还是拆单
                if(machineOrderDetail.getOriginalOrderId() != null && machineOrderDetail.getOriginalOrderId() != 0) {
                    int status = machineOrderService.findById(machineOrderDetail.getOriginalOrderId()).getStatus();
                    if( status == Constant.ORDER_CHANGED.intValue()) {
                        Condition condition = new Condition(OrderChangeRecord.class);
                        condition.createCriteria().andCondition("order_id = ", machineOrderDetail.getOriginalOrderId());
                        List<OrderChangeRecord> list = orderChangeRecordService.findByCondition(condition);
                        if(list != null && list.size() == 1) {
                            cell2 = sheetX.getRow(38 + equipmentCount).getCell((short) 2);
                            cell2.setCellValue(new HSSFRichTextString(list.get(0).getChangeReason()));
                        }
                    } else if(status == Constant.ORDER_SPLITED.intValue()) {
                        // 拆单
                        Condition condition = new Condition(OrderSplitRecord.class);
                        condition.createCriteria().andCondition("order_id = ", machineOrderDetail.getId());
                        List<OrderSplitRecord> list = orderSplitRecordService.findByCondition(condition);
                        if(list != null && list.size() == 1) {
                            cell2 = sheetX.getRow(39 + equipmentCount).getCell((short) 2);
                            cell2.setCellValue(new HSSFRichTextString(list.get(0).getSplitReason()));
                        }
                    }
                }

                /**
                 *  需求单审核信息，来自 order_sign， 具体有几个签核步骤，可以动态填入表格
                 */
                orderSignList = orderSignService.getOrderSignListByOrderId(machineOrderIdList.get(i));

                if (orderSignList.size() > 0) {
                    //取最后一次的签核，后续看是否需要根据时间来取最新
                    orderSign = orderSignList.get(orderSignList.size() - 1);
                    signContentItemList = JSON.parseArray(orderSign.getSignContent(), SignContentItem.class);

                    //需求单的N个签核，插入N行
                    Integer orderSignCount = signContentItemList.size();
                    insertRow2(wb, sheetX, 41 + equipmentCount, orderSignCount);
                    for (int k = 0; k < orderSignCount; k++) {
                        /**
                         * 需求单签核的： 角色（部门）/人/时间/意见
                         */
                        //1.签核角色（部门）
                        int roleId = signContentItemList.get(k).getRoleId();
                        //根据roleId返回角色（部门）
                        String roleName = roleService.findById(roleId).getRoleName();
                        cell = sheetX.getRow(41 + equipmentCount + k).getCell((short) 0);
                        cell.setCellValue(new HSSFRichTextString(roleName));
                        //2.签核人
                        cell = sheetX.getRow(41 + equipmentCount + k).getCell((short) 1);
                        cell.setCellValue(new HSSFRichTextString(signContentItemList.get(k).getUser()));
                        //3.签核时间
                        cell = sheetX.getRow(41 + equipmentCount + k).getCell((short) 2);
                        if (null != signContentItemList.get(k).getDate()) {
                            cell.setCellValue(new HSSFRichTextString(formatter2.format(signContentItemList.get(k).getDate())));
                        }
                        cell = sheetX.getRow(41 + equipmentCount + k).getCell((short) 3);
                        cell.setCellValue(new HSSFRichTextString("意见"));
                        //4.签核意见, 隐藏 成本核算员、财务会计、财务经理，这三个角色的的审批意见
                        cell = sheetX.getRow(41 + equipmentCount + k).getCell((short) 4);
                        if(roleName.equals("成本核算员")||roleName.equals("财务会计")||roleName.equals("财务经理")) {
                            if (displayPrice) {
                                cell.setCellValue(new HSSFRichTextString(signContentItemList.get(k).getComment()));
                            } else if(!signContentItemList.get(k).getComment().isEmpty()) {
                                cell.setCellValue(new HSSFRichTextString("/"));
                            }
                        }else {
                            cell.setCellValue(new HSSFRichTextString(signContentItemList.get(k).getComment()));
                        }
                        //合并单元格
                        sheetX.addMergedRegion(new CellRangeAddress(41 + equipmentCount + k,
                                41 + equipmentCount + k, 4, 10));

                    }
                    //最后删除多余一行
                    sheetX.shiftRows(41 + equipmentCount + orderSignCount + 1 ,
                            sheetX.getLastRowNum(),
                            -1);

                    // 订单类型、
                    cell = sheetX.getRow(41 + equipmentCount + orderSignCount).getCell((short) 1);
                    cell.setCellValue(new HSSFRichTextString(machineOrderDetail.getOrderType()));
                    //业务费、
                    cell = sheetX.getRow(41 + equipmentCount + orderSignCount).getCell((short) 3);
                    if (displayPrice) {
                        cell.setCellValue(new HSSFRichTextString(machineOrderDetail.getBusinessExpense()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString("/"));
                    }
                    //保修费
                    cell = sheetX.getRow(41 + equipmentCount + orderSignCount).getCell((short) 5);
                    if (displayPrice) {
                    cell.setCellValue(new HSSFRichTextString(machineOrderDetail.getWarrantyFee()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString("/"));
                    }
                }
            }

            //修改模板内容导出新模板,生成路径供前端下载
            downloadPath = contractOutputDir + contract.getContractNum().replaceAll("/", "-") + ".xls";
            downloadPathForNginx = "/excel/" + contract.getContractNum().replaceAll("/", "-") + ".xls";
            out = new FileOutputStream(downloadPath);
            wb.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if ("".equals(downloadPath)) {
            return ResultGenerator.genFailResult("生成合同文件失败!");
        } else {
            return ResultGenerator.genSuccessResult(downloadPathForNginx);
        }
    }

    /**
     *
     * @param equipmentNumArr:  装置数量的数组
     * @param i: 第i个订单
     * @return 返回前面i-1个订单的： 装置总数+居间+优惠的总量
     */
    private int getLinesSum(int[] equipmentNumArr, int i){
        int sum = 0;
        if(i == 0){
            /**
             * 第一个订单
             */
            sum = 0;
        } else {
            /**
             * 从第2个订单开始,要加上前面订单所占的行数
             */
            for (int j = 0; j < i ; j++) {
                sum += equipmentNumArr[j];
                sum += 3; //居间费用/优惠/小计各一行，机器本身一行
            }
        }
        if (isDebug) {
            System.out.println("订单_" + i + "前面的行数" + " getLinesSum: " + sum);
        }
        return sum;
    }
    private void insertRow(HSSFWorkbook wb, HSSFSheet sheet, int starRow, int rows) {
        sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);
        starRow = starRow - 1;

        //创建 多 行
        for (int i = 0; i < rows; i++) {
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
            for (m = sourceRow.getFirstCellNum(); m < 5; m++) {

                targetCell = targetRow.createCell(m);
                sourceCell = sourceRow.getCell(m);
                targetCell.setCellStyle(sourceCell.getCellStyle());
                targetCell.setCellType(sourceCell.getCellType());
            }
        }
    }

    /**
     * TODO: insertRow2和insertRow可以合并
     *
     * @param wb
     * @param sheet
     * @param starRow
     * @param rows
     */
    private void insertRow2(HSSFWorkbook wb, HSSFSheet sheet, int starRow, int rows) {
        sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);
        starRow = starRow - 1;

        //创建 多 行
        for (int i = 0; i < rows; i++) {
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
            for (m = sourceRow.getFirstCellNum(); m < 11; m++) {

                targetCell = targetRow.createCell(m);
                sourceCell = sourceRow.getCell(m);
                targetCell.setCellStyle(sourceCell.getCellStyle());
                targetCell.setCellType(sourceCell.getCellType());
            }
        }
    }

    @PostMapping("/selectAllCustomer")
    public Result selectAllCustomer(@RequestParam String name) {
        List<ContractDetail> list = contractService.selectAllCustomer(name);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(list);
    }

    @PostMapping("/isContractExist")
    public Result isContractExist(@RequestParam String contractNum) {
        if (contractNum == null) {
            return ResultGenerator.genFailResult("请输入合同编号！");
        } else {
            List<Contract> list = contractService.isContractExist( contractNum );
            if (list.size() == 0) {
                return ResultGenerator.genSuccessResult();
            } else {
                return ResultGenerator.genFailResult("合同编号已存在！");
            }
        }
    }

    /**
     * 根据订单号 返回合同
     * @param orderNumber
     * @return 应该返回一个， 返回多个的情况是异常。
     */
    @PostMapping("/getContractByOrderNumber")
    public Result getContractByOrderNumber(@RequestParam String orderNumber) {

        List<Contract> list = contractService.getContractByOrderNumber(orderNumber);
        if(list.size() >1){
            return ResultGenerator.genFailResult("该编号存在多个合同");
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }
}