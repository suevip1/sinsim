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
import com.eservice.api.service.ContractService;
import com.eservice.api.service.impl.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

}
