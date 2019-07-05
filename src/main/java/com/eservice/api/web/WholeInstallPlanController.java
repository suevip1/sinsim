package com.eservice.api.web;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.whole_install_plan.WholeInstallPlan;
import com.eservice.api.service.impl.WholeInstallPlanServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/06/22.
*/
@RestController
@RequestMapping("/whole/install/plan")
public class WholeInstallPlanController {
    @Resource
    private WholeInstallPlanServiceImpl wholeInstallPlanService;

    @PostMapping("/add")
    public Result add(WholeInstallPlan wholeInstallPlan) {
        wholeInstallPlanService.save(wholeInstallPlan);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        wholeInstallPlanService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(WholeInstallPlan wholeInstallPlan) {
        wholeInstallPlanService.update(wholeInstallPlan);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        WholeInstallPlan wholeInstallPlan = wholeInstallPlanService.findById(id);
        return ResultGenerator.genSuccessResult(wholeInstallPlan);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<WholeInstallPlan> list = wholeInstallPlanService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //获取所有未发送的总装计划
    @PostMapping("/selectUnSendWIPs")
    public Result selectUnSendWIPs(@RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<WholeInstallPlan> list = wholeInstallPlanService.selectUnSendWIPs();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 发送 所有未发送的总装排产
     */
    @PostMapping("/sendUnDeliveryWIPs")
    public Result sendUnDeliveryWIPs() {
        Result result = wholeInstallPlanService.sendUnDeliveryWIPs();
        return result;
    }
}
