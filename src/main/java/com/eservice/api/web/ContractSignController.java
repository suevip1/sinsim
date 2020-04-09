package com.eservice.api.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.contract_sign.SignContentItem;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.service.ContractService;
import com.eservice.api.service.ContractSignService;
import com.eservice.api.service.OrderSignService;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.common.Utils;
import com.eservice.api.service.impl.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
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
@RequestMapping("/contract/sign")
public class ContractSignController {
    @Resource
    private ContractSignServiceImpl contractSignService;
    @Resource
    private ContractServiceImpl contractService;
    @Resource
    private CommonService commonService;
    @Resource
    private OrderSignServiceImpl orderSignService;
    @Resource
    private MachineOrderServiceImpl machineOrderService;

    @PostMapping("/add")
    public Result add(String contractSign) {
        ContractSign contractSign1 = JSON.parseObject(contractSign, ContractSign.class);
        contractSignService.save(contractSign1);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        contractSignService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    //// 目前已经没有合同签核，合同不用签核，订单要签核
    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public Result update(String contractSign) {
        ContractSign contractSignObj = JSON.parseObject(contractSign, ContractSign.class);
        contractSignObj.setUpdateTime(new Date());
        contractSignService.update(contractSignObj);
        String step = commonService.getCurrentSignStep(contractSignObj.getContractId());
        if (step == null) {
            throw new RuntimeException();
        } else {
            Contract contract = contractService.findById(contractSignObj.getContractId());
            if (step.equals(Constant.SIGN_FINISHED)) {
                //表示签核已经完成
                contract.setStatus(Constant.CONTRACT_CHECKING_FINISHED);

                //需求单也需要设置为签核完成状态“ORDER_CHECKING_FINISHED”
                Condition tempCondition = new Condition(ContractSign.class);
                tempCondition.createCriteria().andCondition("contract_id = ", contract.getId());
                List<MachineOrder> machineOrderList = machineOrderService.findByCondition(tempCondition);
                for (MachineOrder item : machineOrderList) {
                    if (item.getStatus().equals(Constant.ORDER_CHECKING)) {
                        item.setStatus(Constant.ORDER_CHECKING_FINISHED);
                    }
                    machineOrderService.update(item);
                }
                //根据合同中的需求单进行机器添加
                commonService.createMachineByContractId(contractSignObj.getContractId());
            } else {
                //更新合同状态
                List<SignContentItem> contractSignContentList = JSON.parseArray(contractSignObj.getSignContent(), SignContentItem.class);
                boolean haveReject = false;
                for (SignContentItem item : contractSignContentList) {
                    //如果签核内容中有“拒绝”状态的签核信息，需要将该
                    if (item.getResult().equals(Constant.SIGN_REJECT)) {
                        haveReject = true;
                        break;
                    }
                }
                if (haveReject) {
                    contract.setStatus(Constant.CONTRACT_REJECTED);
                    //需要把之前的签核状态result设置为初始状态“SIGN_INITIAL”，但是签核内容不变(contract & machineOrder)
                    //合同相关
                    for (SignContentItem item : contractSignContentList) {
                        item.setResult(Constant.SIGN_INITIAL);
                    }
                    contractSignObj.setSignContent(JSONObject.toJSONString(contractSignContentList));
                    //当前审核步骤变成空
                    step = "";

                    //需求单相关
                    List<OrderSign> orderSignList = orderSignService.getValidOrderSigns(contractSignObj.getContractId());
                    for (OrderSign item : orderSignList) {
                        //之前把正在签核中，或者驳回状态的需求单的签核状态设置为“SIGN_INITIAL”
                        MachineOrder machineOrder = machineOrderService.findById(item.getOrderId());
                        Byte status = machineOrder.getStatus();
                        if (Constant.ORDER_CHECKING.equals(status) || Constant.ORDER_REJECTED.equals(status)) {
                            List<SignContentItem> signContentItemList = JSONObject.parseArray(item.getSignContent(), SignContentItem.class);
                            for (SignContentItem signContentItem : signContentItemList) {
                                signContentItem.setResult(Constant.SIGN_INITIAL);
                            }
                            item.setSignContent(JSONObject.toJSONString(signContentItemList));
                            orderSignService.update(item);
                            //需求单状态设置为“ORDER_REJECTED”
                            machineOrder.setStatus(Constant.ORDER_REJECTED);
                            machineOrderService.update(machineOrder);
                        }
                    }
                } else {//已驳回的重新审核后，更改合同状态为审核中.
                    if (contract.getStatus() == Constant.CONTRACT_REJECTED) {
                        contract.setStatus(Constant.CONTRACT_CHECKING);

                        //需求单也需要重新设置为审核中
                        Condition tempCondition = new Condition(ContractSign.class);
                        tempCondition.createCriteria().andCondition("contract_id = ", contract.getId());
                        List<MachineOrder> machineOrderList = machineOrderService.findByCondition(tempCondition);
                        for (MachineOrder item : machineOrderList) {
                            if (item.getStatus() == Constant.ORDER_REJECTED) {
                                item.setStatus(Constant.ORDER_CHECKING);
                            }
                            machineOrderService.update(item);
                        }
                    }
                }
            }
            contract.setUpdateTime(new Date());
            contractService.update(contract);
        }
        contractSignObj.setCurrentStep(step);
        contractSignService.update(contractSignObj);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        ContractSign contractSign = contractSignService.findById(id);
        return ResultGenerator.genSuccessResult(contractSign);
    }

    @PostMapping("/detailByContractId")
    public Result detailByContractNum(@RequestParam String contractId) {
        ContractSign contractSign = contractSignService.detailByContractId(contractId);
        return ResultGenerator.genSuccessResult(contractSign);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, @RequestParam(defaultValue = "0") Integer contractId) {
        PageHelper.startPage(page, size);
        List<ContractSign> list;
        if (contractId == 0) {
            list = new ArrayList<>();
        } else {
            //获取摸个合同号对应的全部签核记录
            Condition tempCondition = new Condition(ContractSign.class);
            tempCondition.createCriteria().andCondition("contract_id = ", contractId);
            list = contractSignService.findByCondition(tempCondition);
        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
