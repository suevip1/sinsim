package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.whole_install_acutual.WholeInstallAcutual;
import com.eservice.api.model.whole_install_acutual.WholeInstallAcutualDetails;
import com.eservice.api.service.WholeInstallPlanService;
import com.eservice.api.service.impl.WholeInstallAcutualServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
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
* @date 2019/06/22.
*/
@RestController
@RequestMapping("/whole/install/acutual")
public class WholeInstallAcutualController {
    @Resource
    private WholeInstallAcutualServiceImpl wholeInstallAcutualService;

    @Resource
    private WholeInstallPlanService wholeInstallPlanService;

    private Logger logger = Logger.getLogger(WholeInstallAcutualController.class);
    @PostMapping("/add")
    public Result add(String wholeInstallAcutual) {
        WholeInstallAcutual wia = JSON.parseObject(wholeInstallAcutual, WholeInstallAcutual.class);
        if (wia != null) {
            if (wia.getWholeInstallPlanId() == null) {
                return ResultGenerator.genFailResult("错误，WholeInstallPlanId为null！");
            } else if (wholeInstallPlanService.findById(wia.getWholeInstallPlanId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该 WholeInstallPlanId " + wia.getWholeInstallPlanId() + " 找不到对应的plan ！");
            } else {
                wia.setCreateDate(new Date());
                wholeInstallAcutualService.save(wia);
            }
        } else {
           return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        wholeInstallAcutualService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String wholeInstallAcutual) {

        WholeInstallAcutual wia = JSON.parseObject(wholeInstallAcutual,WholeInstallAcutual.class);
        if (wia != null) {
            if (wia.getWholeInstallPlanId() == null) {
                return ResultGenerator.genFailResult("错误，WholeInstallPlanId为null！");
            } else if (wholeInstallPlanService.findById(wia.getWholeInstallPlanId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该 WholeInstallPlanId " + wia.getWholeInstallPlanId() + " 找不到对应的plan ！");
            } else {
                wia.setUpdateDate(new Date());
                wholeInstallAcutualService.update(wia);
            }
        } else {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        WholeInstallAcutual wholeInstallAcutual = wholeInstallAcutualService.findById(id);
        return ResultGenerator.genSuccessResult(wholeInstallAcutual);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<WholeInstallAcutual> list = wholeInstallAcutualService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
	
    @PostMapping("/selectWholeInstallDetails")
    public Result selectWholeInstallDetails(@RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "0") Integer size,
                                     String orderNum,
                                     String nameplate,
                                     String installGroupName) {
        PageHelper.startPage(page, size);
        List<WholeInstallAcutualDetails> list = wholeInstallAcutualService.selectWholeInstallDetails(orderNum,nameplate,installGroupName);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
