package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract.Contract;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.machine_order.MachineOrderDetail;
import com.eservice.api.service.ContractService;
import com.eservice.api.service.ContractSignService;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.common.Utils;
import com.eservice.api.service.impl.ContractSignServiceImpl;
import com.eservice.api.service.impl.MachineOrderServiceImpl;
import com.eservice.api.service.impl.MachineServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2017/12/07.
*/
@RestController
@RequestMapping("/contract/sign")
public class ContractSignController {
    @Resource
    private ContractSignServiceImpl contractSignService;

    @Resource
    private CommonService commonService;

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

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public Result update(String contractSign) {
        ContractSign contractSign1 = JSON.parseObject(contractSign,ContractSign.class);
        contractSign1.setUpdateTime(new Date());
        contractSignService.update(contractSign1);
        String step = commonService.getCurrentSignStep(contractSign1.getContractId());
        if(step == null) {
            throw new RuntimeException();
        }else if(step.equals(Constant.SIGN_FINISHED)) {
            //表示签核已经完成
            contractSign1.setStatus(Byte.parseByte("2"));
            //根据合同中的需求单进行机器添加
            commonService.createMachineByContractId(contractSign1.getContractId());
        }
        contractSign1.setCurrentStep(step);
        contractSignService.update(contractSign1);
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
        if(contractId == 0) {
            list = contractSignService.findAll();
        }else {
            //获取摸个合同号对应的全部签核记录
            Condition tempCondition = new Condition(ContractSign.class);
            tempCondition.createCriteria().andCondition("contract_id = ", contractId);
            list = contractSignService.findByCondition(tempCondition);
        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
