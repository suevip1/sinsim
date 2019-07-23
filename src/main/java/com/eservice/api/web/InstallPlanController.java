package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.install_plan.InstallPlan;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.service.InstallPlanService;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.service.MachineService;
import com.eservice.api.service.impl.InstallGroupServiceImpl;
import com.eservice.api.service.impl.InstallPlanServiceImpl;
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
* @date 2019/07/19.
*/
@RestController
@RequestMapping("/install/plan")
public class InstallPlanController {
    @Resource
    private InstallPlanServiceImpl installPlanService;

    @Resource
    private InstallGroupServiceImpl installGroupService;

    @Resource
    private MachineOrderService machineOrderService;

    @Resource
    private MachineService machineService;

    private Logger logger = Logger.getLogger(InstallPlanController.class);
    @PostMapping("/add")
    public Result add(String installPlan) {

        InstallPlan installPlan1 = JSON.parseObject(installPlan, InstallPlan.class);

        /**
         * 逐一检查各个必要参数的合法性
         */
        if (installPlan1 != null) {
            if (installPlan1.getInstallGroupId() == null) {
                return ResultGenerator.genFailResult("错误，getInstallGroupId 为 null！");
            } else if (installGroupService.findById(installPlan1.getInstallGroupId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该InstallGroupId " + installPlan1.getInstallGroupId() + " 找不到对应的 installGroup！");
            }

            if (installPlan1.getInstallDatePlan() == null) {
                return ResultGenerator.genFailResult("错误，InstallDatePlan 为 null！");
            }

            if (installPlan1.getOrderId() == null) {
                return ResultGenerator.genFailResult("错误，orderId 为 null！");
            } else if(machineOrderService.findById(installPlan1.getOrderId()) == null){
                return ResultGenerator.genFailResult("错误，根据该 orderId " + installPlan1.getOrderId() + " 找不到对应的 machineOrder ！");
            }

            /**
             * 检查machine是否存在，并且和订单匹配
             */
            if (installPlan1.getMachineId() == null) {
                return ResultGenerator.genFailResult("错误，machineId 为 null！");
            }  else if(machineService.findById(installPlan1.getMachineId()) == null){
                return ResultGenerator.genFailResult("错误，根据该 machineId " + installPlan1.getMachineId() + " 找不到对应的 machine ！");
            } else if( !isMachineInTheOrder(installPlan1.getMachineId(), installPlan1.getOrderId())) {
                return ResultGenerator.genFailResult("错误， 该机器是不属于该订单");
            }

            installPlan1.setCreateDate(new Date());
            installPlanService.save(installPlan1);

        } else {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }
    /**
     *  确认该机器是否归属该订单
     * @return
     */
    private Boolean isMachineInTheOrder(Integer machineId, Integer OrderId){
        MachineOrder machineOder = machineOrderService.findById(OrderId);
        if(machineOder == null){
            logger.info("错误，根据该 orderId " + OrderId + " 找不到对应的 machineOrder ！");
            return false;
        } else {
            //todo
        }
        return true;
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        installPlanService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update( String installPlan) {
        InstallPlan installPlan1 = JSON.parseObject(installPlan, InstallPlan.class);

        if (installPlan1 != null) {
            if(installPlan1.getId() == null) {
                return ResultGenerator.genFailResult("错误，installPlan1 id  为 null！");
            }
            if (installPlan1.getInstallGroupId() == null) {
                return ResultGenerator.genFailResult("错误，getInstallGroupId 为 null！");
            } else if (installGroupService.findById(installPlan1.getInstallGroupId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该InstallGroupId " + installPlan1.getInstallGroupId() + " 找不到对应的 installGroup！");
            }

            if (installPlan1.getInstallDatePlan() == null) {
                return ResultGenerator.genFailResult("错误，InstallDatePlan 为 null！");
            }

            if (installPlan1.getOrderId() == null) {
                return ResultGenerator.genFailResult("错误，orderId 为 null！");
            } else if(machineOrderService.findById(installPlan1.getOrderId()) == null){
                return ResultGenerator.genFailResult("错误，根据该 orderId " + installPlan1.getOrderId() + " 找不到对应的 machineOrder ！");
            }

            /**
             * 检查machine是否存在，并且和订单匹配
             */
            if (installPlan1.getMachineId() == null) {
                return ResultGenerator.genFailResult("错误，machineId 为 null！");
            }  else if(machineService.findById(installPlan1.getMachineId()) == null){
                return ResultGenerator.genFailResult("错误，根据该 machineId " + installPlan1.getMachineId() + " 找不到对应的 machine ！");
            } else if( !isMachineInTheOrder(installPlan1.getMachineId(), installPlan1.getOrderId())) {
                return ResultGenerator.genFailResult("错误， 该机器是不属于该订单");
            }

            installPlan1.setCreateDate(new Date());
            installPlanService.update(installPlan1);

        } else {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        InstallPlan installPlan = installPlanService.findById(id);
        return ResultGenerator.genSuccessResult(installPlan);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InstallPlan> list = installPlanService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //获取所有未发送的排产计划
    @PostMapping("/selectUnSendInstallPlans")
    public Result selectUnSendInstallPlans(@RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<InstallPlan> list = installPlanService.selectUnSendInstallPlans();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 发送 所有未发送的排产计划
     */
    @PostMapping("/sendUnDeliveryWIPs")
    public Result sendUnDeliveryWIPs() {
        Result result = installPlanService.sendUnDeliveryInstallPlans();
        return result;
    }

}
