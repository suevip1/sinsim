package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.contract_sign.SignContentItem;
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
            String currentStep = "";
            for (SignContentItem item : orderSignContentList) {
                //如果签核内容中有“拒绝”状态的签核信息，需要将该
                if (item.getResult().equals(Constant.SIGN_REJECT)) {
                    haveReject = true;
                }
                if(item.getResult() == 0) {
                    currentStep = roleService.findById(item.getRoleId()).getRoleName();
                    break;
                }
            }

            //都已经签核
            if(!haveReject) {
                if(currentStep.equals("")) {
                    currentStep = Constant.SIGN_FINISHED;
                }
                orderSignObj.setCurrentStep(currentStep);
            }
            orderSignService.update(orderSignObj);


            MachineOrder machineOrder = machineOrderService.findById(orderSignObj.getOrderId());
            Contract contract = contractService.findById(contractId);

            commonService.pushMachineOrderMsgToAftersale(orderSignObj,contract,machineOrder,haveReject);

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
            } else {
                if (machineOrder.getStatus().equals(Constant.ORDER_INITIAL)) {
                    machineOrder.setStatus(Constant.ORDER_CHECKING);
                }
                //需求单签核完成
                if(currentStep.equals(Constant.SIGN_FINISHED)) {
                    machineOrder.setStatus(Constant.ORDER_CHECKING_FINISHED);
                    commonService.createMachineByOrderId(machineOrder);
                }
            }
            machineOrderService.update(machineOrder);

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
     * @param page
     * @param size
     * @param orderId
     * @return
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
