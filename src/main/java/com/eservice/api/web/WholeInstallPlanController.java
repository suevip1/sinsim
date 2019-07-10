package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.whole_install_plan.WholeInstallPlan;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.service.MachineService;
import com.eservice.api.service.TaskService;
import com.eservice.api.service.impl.WholeInstallPlanServiceImpl;
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
@RequestMapping("/whole/install/plan")
public class WholeInstallPlanController {
    @Resource
    private WholeInstallPlanServiceImpl wholeInstallPlanService;

    @Resource
    private TaskService taskService;

    @Resource
    private MachineOrderService machineOrderService;

    @Resource
    private MachineService machineService;

    private Logger logger = Logger.getLogger(WholeInstallAcutualController.class);

    @PostMapping("/add")
    public Result add(String wholeInstallPlan) {
        WholeInstallPlan wip = JSON.parseObject(wholeInstallPlan, WholeInstallPlan.class);
        /**
         * 逐一检查各个必要参数的合法性
         */
        if (wip != null) {
            if (wip.getInstallTaskId() == null) {
                return ResultGenerator.genFailResult("错误，InstallTaskId 为 null！");
            } else if (taskService.findById(wip.getInstallTaskId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该 InstallTaskId " + wip.getInstallTaskId() + " 找不到对应的task ！");
            }

            if (wip.getInstallDatePlan() == null) {
                return ResultGenerator.genFailResult("错误，InstallDatePlan 为 null！");
            }

            if (wip.getOrderId() == null) {
                return ResultGenerator.genFailResult("错误，orderId 为 null！");
            } else if(machineOrderService.findById(wip.getOrderId()) == null){
                return ResultGenerator.genFailResult("错误，根据该 orderId " + wip.getOrderId() + " 找不到对应的 machineOrder ！");
            }
            /**
             * 检查machine是否存在，并且和订单匹配
             */
            if (wip.getMachineId() == null) {
                return ResultGenerator.genFailResult("错误，machineId 为 null！");
            }  else if(machineService.findById(wip.getMachineId()) == null){
                return ResultGenerator.genFailResult("错误，根据该 machineId " + wip.getMachineId() + " 找不到对应的 machine ！");
            } else if( !isMachineInTheOrder(wip.getMachineId(), wip.getOrderId())) {
                return ResultGenerator.genFailResult("错误， 该机器是不属于该订单");
            }

            wip.setCreateDate(new Date());
            wholeInstallPlanService.save(wip);

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
        wholeInstallPlanService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update( String wholeInstallPlan) {
        WholeInstallPlan wip = JSON.parseObject(wholeInstallPlan, WholeInstallPlan.class);
        if (wip != null) {
            if(wip.getId() == null) {
                return ResultGenerator.genFailResult("错误，wholeInstallPlan id  为 null！");
            }

            if (wip.getInstallTaskId() == null) {
                return ResultGenerator.genFailResult("错误，InstallTaskId 为 null！");
            } else if (taskService.findById(wip.getInstallTaskId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该 InstallTaskId " + wip.getInstallTaskId() + " 找不到对应的task ！");
            }

            if (wip.getInstallDatePlan() == null) {
                return ResultGenerator.genFailResult("错误，InstallDatePlan 为 null！");
            }

            if (wip.getOrderId() == null) {
                return ResultGenerator.genFailResult("错误，orderId 为 null！");
            } else if (machineOrderService.findById(wip.getOrderId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该 orderId " + wip.getOrderId() + " 找不到对应的 machineOrder ！");
            }

            /**
             * 检查machine是否存在，并且和订单匹配
             */
            if (wip.getMachineId() == null) {
                return ResultGenerator.genFailResult("错误，machineId 为 null！");
            } else if (machineService.findById(wip.getMachineId()) == null) {
                return ResultGenerator.genFailResult("错误，根据该 machineId " + wip.getMachineId() + " 找不到对应的 machine ！");
            } else if (!isMachineInTheOrder(wip.getMachineId(), wip.getOrderId())) {
                return ResultGenerator.genFailResult("错误， 该机器是不属于该订单");
            }
            wip.setUpdateDate(new Date());
            wholeInstallPlanService.update(wip);

        } else {
            return ResultGenerator.genFailResult("参数不正确，添加失败！");
        }
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
