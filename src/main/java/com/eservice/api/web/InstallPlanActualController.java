package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.install_plan_actual.InstallPlanActual;
import com.eservice.api.model.install_plan_actual.InstallPlanActualDetails;
import com.eservice.api.service.InstallPlanActualService;
import com.eservice.api.service.InstallPlanService;
import com.eservice.api.service.impl.InstallPlanActualServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
* @date 2019/07/19.
*/
@RestController
@RequestMapping("/install/plan/actual")
public class InstallPlanActualController {
    @Resource
    private InstallPlanActualServiceImpl installPlanActualService;

    @Resource
    private InstallPlanService installPlanService;

    @PostMapping("/add")
    public Result add(String installPlanActual) {
        InstallPlanActual installPlanActual1 = JSON.parseObject(installPlanActual, InstallPlanActual.class);
        if (installPlanActual1 != null) {
            if (installPlanActual1.getInstallPlanId() == null) {
                return ResultGenerator.genFailResult("错误，InstallPlanId为null！");
            } else if (installPlanService.findById(installPlanActual1.getInstallPlanId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该InstallPlanId " + installPlanActual1.getInstallPlanId() + " 找不到对应的plan ！");
            } else {
                installPlanActual1.setCreateDate(new Date());
                installPlanActualService.save(installPlanActual1);
            }
        } else {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        installPlanActualService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String installPlanActual) {
        InstallPlanActual ipa = JSON.parseObject(installPlanActual,InstallPlanActual.class);
        if (ipa != null) {
            if (ipa.getInstallPlanId() == null) {
                return ResultGenerator.genFailResult("错误，InstallPlanId为null！");
            } else if (installPlanService.findById(ipa.getInstallPlanId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该InstallPlanId " + ipa.getInstallPlanId() + " 找不到对应的plan ！");
            } else {
                ipa.setUpdateDate(new Date());
                installPlanActualService.update(ipa);
            }
        } else {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        InstallPlanActual installPlanActual = installPlanActualService.findById(id);
        return ResultGenerator.genSuccessResult(installPlanActual);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InstallPlanActual> list = installPlanActualService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 根据条件查询排产计划及其完成情况细节。
     * @param page
     * @param size
     * @param orderNum
     * @param nameplate
     * @param installGroupName
     * @return
     */
    @PostMapping("/selectInstallPlanActualDetails")
    public Result selectInstallPlanActualDetails(@RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "0") Integer size,
                                            String orderNum,
                                            String nameplate,
                                            String installGroupName) {
        PageHelper.startPage(page, size);
        List<InstallPlanActualDetails> list = installPlanActualService.selectInstallPlanActualDetails(orderNum,nameplate,installGroupName);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}