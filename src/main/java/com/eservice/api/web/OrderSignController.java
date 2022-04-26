package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.design_dep_info.DesignDepInfoDetail;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.model.role.Role;
import com.eservice.api.model.user.User;
import com.eservice.api.model.user.UserDetail;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2017/11/14.
 */
@RestController
@RequestMapping("/order/sign")
public class OrderSignController {
    @Resource
    private OrderSignServiceImpl orderSignService;
    @Resource
    private CommonService commonService;
    @Resource
    private ContractSignServiceImpl contractSignService;
    @Resource
    private ContractServiceImpl contractService;
    @Resource
    private MachineOrderServiceImpl machineOrderService;
    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private UserServiceImpl userService;
    @Resource
    private DesignDepInfoServiceImpl designDepInfoService;

    private Logger logger = Logger.getLogger(OrderSignController.class);

    @PostMapping("/add")
    public Result add(String orderSign) {
        OrderSign orderSign1 = JSON.parseObject(orderSign, OrderSign.class);
        orderSignService.save(orderSign1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        orderSignService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public Result update(Integer contractId, String orderSign) {
        if (orderSign == null || "".equals(orderSign)) {
            ResultGenerator.genFailResult("签核信息为空！");
        }
        if (contractId == null || contractId <= 0) {
            ResultGenerator.genFailResult("合同ID不存在或者无效！");
        }
        OrderSign orderSignObj = JSONObject.parseObject(orderSign, OrderSign.class);
        if (orderSignObj == null) {
            ResultGenerator.genFailResult("签核信息JSON解析失败！");
        } else {
            //更新需求单签核记录
            orderSignObj.setUpdateTime(new Date());
            //更新需求单状态
            List<SignContentItem> orderSignContentList = JSON.parseArray(orderSignObj.getSignContent(), SignContentItem.class);
            boolean haveReject = false;
            boolean needMakeUpOrderSignForForeignDirector = false;
            String currentStep = "";

            MachineOrder machineOrder = machineOrderService.findById(orderSignObj.getOrderId());
            User machineOrderCreator = userService.findById(machineOrder.getCreateUserId());
            for (SignContentItem item : orderSignContentList) {
                //如果签核内容中有“拒绝”状态的签核信息，需要将该
                if (item.getResult().equals(Constant.SIGN_REJECT)) {
                    haveReject = true;
                }
                
                if(item.getResult() == Constant.SIGN_INITIAL  ) {
                    currentStep = roleService.findById(item.getRoleId()).getRoleName();
                    break;
                }
                /**
                 * 订单审核完成时，创建设计单 2020-1207改了：
                 * 如果是技术部经理签核且通过，则要生成对应的设计单
                 * 并且没有生成过联系单
                 */
                if(item.getRoleId() == Constant.ROLE_ID_TECH_MANAGER
                        && item.getResult().equals(Constant.SIGN_APPROVE)){
                    logger.info("技术部经理签核，且通过");
                    // 查找订单对应的设计单
                    List<DesignDepInfoDetail> designDepInfoDetailList = designDepInfoService.selectDesignDepInfo(
                            machineOrder.getOrderNum(),
                            null,
                            null,
                            Integer.valueOf(Constant.ORDER_CHECKING), //审核中
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                    if(designDepInfoDetailList == null || designDepInfoDetailList.size() ==0) {
                        commonService.createDesignDepInfo(machineOrder);
                    }
                }
                /**
                 * 技术部签核，更新相关内容：
                 * 1. 机架长度 来自于技术部经理的签核内容，从“长度”字符开始截取。
                 */
                if(item.getRoleId() == Constant.ROLE_ID_TECH_MANAGER){
                    String commentOfTechManager = item.getComment();
                    int start = 0;
                    start = commentOfTechManager.indexOf("机架长度");
                    if(start == -1) {
                        start = commentOfTechManager.indexOf("【长度】");
                    }
                    if(start == -1) {
                        start = commentOfTechManager.indexOf("[长度]");
                    }
                    if(start == -1) {
                        start = commentOfTechManager.indexOf("长度");
                    }

                    if(start != -1) {
                        machineOrder.setMachineFrameLength(commentOfTechManager.substring(start));
                        machineOrderService.update(machineOrder);
                        logger.info("更新了机架长度为: " + commentOfTechManager.substring(start));
                    }

                }
                /**
                 * 成本核算员签核，更新相关内容：
                 * 1. 毛利率 --已弃用，改为在订单里填写。
                 */
//                if(item.getRoleId() == Constant.ROLE_ID_COST_ACCOUNTANT){
//                    String grossProfitString = "";
//                        int start = 0;
//                        start = item.getComment().indexOf("【毛利率】");
//                        if(start == -1) {
//                            start = item.getComment().indexOf("[毛利率]");
//                        }
//                        if(start == -1) {
//                            start = item.getComment().indexOf("毛利率");
//                        }
//                        if(start != -1) {
//                            grossProfitString = item.getComment().substring(start);
//                            machineOrder.setGrossProfit(grossProfitString);
//                            machineOrderService.update(machineOrder);
//                            logger.info("更新了毛利率为: " + grossProfitString );
//                        }
//                }

                /**
                 * 外贸部特殊情况，【外贸部销售经理】审核之后，再由【外贸部总监】审核
                 * 非外贸部，不需要经过【外贸部总监】审核
                 */
                if(orderSignObj.getCurrentStep().equals(Constant.SING_STEP_SALES_MANAGER)
                && item.getRoleId() == Constant.ROLE_ID_SALES_MANAGER
                        && item.getResult().equals(Constant.SIGN_APPROVE) ) {
                    User userSalesManager = userService.selectByAccount(item.getUser());
                    if(userSalesManager.getMarketGroupName().equals("外贸一部") || userSalesManager.getMarketGroupName().equals("外贸二部")){
                        logger.info("外贸部经理签核，且通过，需要【外贸部总监】审核");

                    } else {
                        /**
                         * 内贸部的订单不需要外贸总监审核
                         * 外贸总监已经在流程中，把数据补充填上，流程往后走
                         */
                        needMakeUpOrderSignForForeignDirector = true;
                    }
                }
            }

            //都已经签核
            if(!haveReject) {
                if (currentStep.equals("")) {
                    currentStep = Constant.SIGN_FINISHED;
                }
                orderSignObj.setCurrentStep(currentStep);
                orderSignService.update(orderSignObj);

                if (needMakeUpOrderSignForForeignDirector) {
                    makeUpSignContent(orderSignObj);
                }
            }

            /**
             * 推送公众号消息给轮到的人（通过售后系统）
             */
            Contract contract = contractService.findById(contractId);
            String msgInfo = null;
            if (haveReject) {
                machineOrder.setStatus(Constant.ORDER_REJECTED);
                //需要把之前的签核状态result设置为初始状态“SIGN_INITIAL”，但是签核内容不变(contract & machineOrder)
                //新需求:合同相关不处理
//                ContractSign contractSignObj = contractSignService.detailByContractId(String.valueOf(contractId));
//                List<SignContentItem> contractSignList = JSON.parseArray(contractSignObj.getSignContent(), SignContentItem.class);
//                for (SignContentItem item : contractSignList) {
//                    item.setResult(Constant.SIGN_INITIAL);
//                }
//                contractSignObj.setSignContent(JSONObject.toJSONString(contractSignList));
//                //当前审核步骤变成空
//                contractSignObj.setCurrentStep("");
//                contractSignService.update(contractSignObj);

                ///需求单相关，当前需求单审核变为初始化“SIGN_INITIAL”
                for (SignContentItem item : orderSignContentList) {
                    item.setResult(Constant.SIGN_INITIAL);
                }
                orderSignObj.setSignContent(JSONObject.toJSONString(orderSignContentList));
                orderSignService.update(orderSignObj);

                //如果有订单驳回，则设置合同为initial状态
                contract.setStatus(Constant.CONTRACT_INITIAL);
                msgInfo = Constant.STR_MSG_PUSH_SIGN_REFUESED;
            } else {
                if (machineOrder.getStatus().equals(Constant.ORDER_INITIAL)) {
                    machineOrder.setStatus(Constant.ORDER_CHECKING);
                }
                //需求单签核完成
                if(currentStep.equals(Constant.SIGN_FINISHED)) {
                    machineOrder.setStatus(Constant.ORDER_CHECKING_FINISHED);
                    commonService.createMachineByOrderId(machineOrder);
                    msgInfo = Constant.STR_MSG_PUSH_SIGN_DONE;
                } else {
                    msgInfo =  Constant.STR_MSG_PUSH_IS_TURN_TO_SIGN;
                }
            }
            machineOrderService.update(machineOrder);
            commonService.syncMachineOrderStatusInDesignDepInfo(machineOrder);
            //推送消息
            commonService.pushMachineOrderMsgToAftersale(orderSignObj,contract,machineOrder,haveReject, msgInfo);

            //更新合同签核记录
            String step = commonService.getCurrentSignStep(contractId);
            ContractSign contractSign = contractSignService.detailByContractId(String.valueOf(contractId));
            if (step == null || contractSign == null) {
                throw new RuntimeException();
            } else {
                if (step.equals(Constant.SIGN_FINISHED)) {
                    //表示签核已经完成，合同设置“CONTRACT_CHECKING_FINISHED”
                    contract.setStatus(Constant.CONTRACT_CHECKING_FINISHED);

//                    //需求单也需要设置为签核完成状态“ORDER_CHECKING_FINISHED”
//                    Condition tempCondition = new Condition(ContractSign.class);
//                    tempCondition.createCriteria().andCondition("contract_id = ", contractId);
//                    List<MachineOrder> machineOrderList = machineOrderService.findByCondition(tempCondition);
//                    for (MachineOrder item : machineOrderList) {
//                        if (item.getStatus().equals(Constant.ORDER_CHECKING)) {
//                            item.setStatus(Constant.ORDER_CHECKING_FINISHED);
//                        }
//                        machineOrderService.update(item);
//                    }

                    //根据合同中的需求单进行机器添加, 在需求单签核、合同签核都加上是因为最后一步审核可能是需求单，也可能是合同
                    //commonService.createMachineByContractId(contractId);
                }
//                else {
//                    if (haveReject) {
//                        contract.setStatus(Constant.CONTRACT_REJECTED);
//                    } else if (contract.getStatus().equals(Constant.CONTRACT_REJECTED)) {
//                        contract.setStatus(Constant.CONTRACT_CHECKING);
//                    }
//                }
                contract.setUpdateTime(new Date());
                contractService.update(contract);
            }
//            contractSign.setCurrentStep(step);
//            contractSignService.update(contractSign);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * workaround, 补充外贸总监的签核，并把currentStep移到下一步
     * 内贸部订单时，补充外贸总监的签核 SIGN_NO_NEED
     */
    public void makeUpSignContent(OrderSign orderSign){
        String currentStep = "";
        List<SignContentItem> orderSignContentList = JSON.parseArray(orderSign.getSignContent(), SignContentItem.class);
        for (SignContentItem item : orderSignContentList){
            if(item.getRoleId() == Constant.ROLE_ID_FOREIGN_DIRECTOR){
                item.setDate(new Date());
                item.setUser("--");
                // * "0" --> "初始化" 没有经过签核不要用1，否则推送消息时会推送
                item.setResult(Constant.SIGN_NO_NEED);
                item.setComment("--");
            }

            // 外贸总监之后 找到下一个作为当前步骤
            if(item.getResult() == Constant.SIGN_INITIAL && item.getRoleId() != Constant.ROLE_ID_FOREIGN_DIRECTOR) {
                currentStep = roleService.findById(item.getRoleId()).getRoleName();
                break;
            }
        }
        orderSign.setSignContent(JSONObject.toJSONString(orderSignContentList));

        orderSign.setCurrentStep(currentStep);
        orderSignService.update(orderSign);
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        OrderSign orderSign = orderSignService.findById(id);
        return ResultGenerator.genSuccessResult(orderSign);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<OrderSign> list = orderSignService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据orderId获取签核信息
     *
     * @param orderId
     * @return  订单的签核记录只有在新增合同-订单时，才创建订单的签核记录，所以这里其实只返回一个签核记录，先保留这样用list
     *          （之前考虑过比如驳回造成的多次签核的分别放在不同的签核记录里，最后其实是放在同个签核记录里）
     */
    @PostMapping("/getOrderSignListByOrderId")
    public Result getOrderSignListByOrderId(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                                            @RequestParam Integer orderId) {
        PageHelper.startPage(page, size);
        List<OrderSign> list = orderSignService.getOrderSignListByOrderId(orderId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
