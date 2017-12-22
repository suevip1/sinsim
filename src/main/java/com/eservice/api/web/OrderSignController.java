package com.eservice.api.web;
import com.alibaba.fastjson.JSONObject;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.contract_sign.ContractSign;
import com.eservice.api.model.order_sign.OrderSign;
import com.eservice.api.service.ContractService;
import com.eservice.api.service.ContractSignService;
import com.eservice.api.service.OrderSignService;
import com.eservice.api.service.common.CommonService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.ContractSignServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
* @author Wilson Hu
* @date 2017/11/14.
*/
@RestController
@RequestMapping("/order/sign")
public class OrderSignController {
    @Resource
    private OrderSignService orderSignService;
    @Resource
    private CommonService commonService;
    @Resource
    private ContractSignServiceImpl contractSignService;

    @PostMapping("/add")
    public Result add(OrderSign orderSign) {
        orderSignService.save(orderSign);
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
        if(orderSign == null || "".equals(orderSign)) {
            ResultGenerator.genFailResult("签核信息为空！");
        }
        if(contractId == null || contractId <=0) {
            ResultGenerator.genFailResult("合同ID不存在或者无效！");
        }
        OrderSign orderSign1 = JSONObject.parseObject(orderSign, OrderSign.class);
        if(orderSign1 == null) {
            ResultGenerator.genFailResult("签核信息JSON解析失败！");
        }else {
            orderSign1.setUpdateTime(new Date());
            orderSignService.update(orderSign1);
            String step = commonService.getCurrentSignStep(contractId);
            ContractSign contractSign = contractSignService.detailByContractId(String.valueOf(contractId));
            if(step == null || contractSign == null) {
                throw new RuntimeException();
            }else if(step.equals(Constant.SIGN_FINISHED)) {
                //表示签核已经完成
                contractSign.setStatus(Byte.parseByte("2"));
            }
            contractSign.setCurrentStep(step);
            contractSignService.update(contractSign);
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
}
