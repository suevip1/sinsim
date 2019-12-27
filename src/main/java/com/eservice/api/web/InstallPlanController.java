package com.eservice.api.web;
import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultCode;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.model.install_group.InstallGroup;
import com.eservice.api.model.install_plan.InstallPlan;
import com.eservice.api.model.install_plan_actual.InstallPlanActualDetails;
import com.eservice.api.model.machine.Machine;
import com.eservice.api.model.machine_order.MachineOrder;
import com.eservice.api.model.user.UserDetail;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.service.MachineService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.impl.InstallGroupServiceImpl;
import com.eservice.api.service.impl.InstallPlanActualServiceImpl;
import com.eservice.api.service.impl.InstallPlanServiceImpl;
import com.eservice.api.service.impl.UserServiceImpl;
import com.eservice.api.service.mqtt.MqttMessageHelper;
import com.eservice.api.service.mqtt.ServerToClientMsg;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

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

    @Resource
    private InstallPlanActualServiceImpl installPlanActualService;

    private Logger logger = Logger.getLogger(InstallPlanController.class);

    @Resource
    private MqttMessageHelper mqttMessageHelper;

    @Resource
    private UserServiceImpl userService;

    @PostMapping("/add")
    public Result add(String installPlan) {

        Result result = checkTheInstallPlanIsSet(installPlan);
        if (result.getCode() == ResultCode.FAIL.code) {
            logger.warn("不合法的installPlan: " + result.getMessage());
            return result;
        }
        InstallPlan installPlan1 = JSON.parseObject(installPlan, InstallPlan.class);
        installPlan1.setCreateDate(new Date());
        installPlanService.save(installPlan1);

        Machine machine = machineService.findById(installPlan1.getMachineId());
        InstallGroup installGroup = installGroupService.findById(installPlan1.getInstallGroupId());
        logger.info("add install plan, nameplate： " + machine.getNameplate()
                + ",组： " + installGroup.getGroupName() + ", date: " + installPlan1.getInstallDatePlan());

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 一次性添加多个排产。
     * @return
     */
//    @PostMapping("/addInstallPlanList")
//    public Result addInstallPlanList(
////            @RequestBody List<String> installPlanList
//              @RequestParam(value = "installPlanList") List<InstallPlan> installPlanList
//            ) {
//
//
//        Result result = null;
//        logger.warn("aaaaaaaaa:" + installPlanList.get(0));
//        return ResultGenerator.genSuccessResult();
//    }

    //是否可以安排该机器该安装组（用于检查一些不合法的安排）
    boolean isOKtoSetPlan(Integer installGroupId, Integer machineId) {
        InstallGroup installGroup = installGroupService.findById(installGroupId);
        if (installGroup == null) {
            logger.warn("Can not find the installGroup by id " + installGroupId);
            return false;
        }
        Machine machine = machineService.findById(machineId);
        if (machine == null) {
            logger.warn("Can not find the machine by id " + machineId);
            return false;
        }
        List<InstallPlanActualDetails> list = installPlanActualService.selectInstallPlanActualDetails(null,
                machine.getNameplate(),
                installGroup.getGroupName(),
                null,
                null,
                null,
                null);
        if (list == null || list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("/checkTheInstallPlanIsSet")
    public Result checkTheInstallPlanIsSet(String installPlan) {
        InstallPlan installPlan1 = JSON.parseObject(installPlan, InstallPlan.class);
        if (installPlan1 == null) {
            return ResultGenerator.genFailResult("参数installPlan1不能为null ");
        }
        /**
         * 逐一检查各个必要参数的合法性
         */
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
        } else if (machineOrderService.findById(installPlan1.getOrderId()) == null) {
            return ResultGenerator.genFailResult("错误，根据该 orderId " + installPlan1.getOrderId() + " 找不到对应的 machineOrder ！");
        }

        /**
         * 检查machine是否存在，并且和订单匹配
         */
        if (installPlan1.getMachineId() == null) {
            return ResultGenerator.genFailResult("错误，machineId 为 null！");
        } else if (machineService.findById(installPlan1.getMachineId()) == null) {
            return ResultGenerator.genFailResult("错误，根据该 machineId " + installPlan1.getMachineId() + " 找不到对应的 machine ！");
        } else if (!isMachineInTheOrder(installPlan1.getMachineId(), installPlan1.getOrderId())) {
            return ResultGenerator.genFailResult("错误， 该机器是不属于该订单");
        }

        //检查该机器是否已经安排了该安装组的计划。
        InstallGroup installGroup = installGroupService.findById(installPlan1.getInstallGroupId());
        Machine machine = machineService.findById(installPlan1.getMachineId());
        if (!isOKtoSetPlan(installGroup.getId(), machine.getId())) {
            logger.warn("该机器 " + machine.getNameplate() + " 已经安排了 " + installGroup.getGroupName());
            return ResultGenerator.genFailResult("该机器 " + machine.getNameplate() + " 已经安排了 " + installGroup.getGroupName());
        }

        return ResultGenerator.genSuccessResult("该排产允许添加");
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

            /**
             * 取消这部分检查，因为update时允许只update部分内容，不需要全部都update.
             */
//            if(installPlan1.getId() == null) {
//                return ResultGenerator.genFailResult("错误，installPlan1 id  为 null！");
//            }
//            if (installPlan1.getInstallGroupId() == null) {
//                return ResultGenerator.genFailResult("错误，getInstallGroupId 为 null！");
//            } else if (installGroupService.findById(installPlan1.getInstallGroupId()) == null) {
//                return ResultGenerator.genFailResult("错误，根据该InstallGroupId " + installPlan1.getInstallGroupId() + " 找不到对应的 installGroup！");
//            }
//
//            if (installPlan1.getInstallDatePlan() == null) {
//                return ResultGenerator.genFailResult("错误，InstallDatePlan 为 null！");
//            }
//
//            if (installPlan1.getOrderId() == null) {
//                return ResultGenerator.genFailResult("错误，orderId 为 null！");
//            } else if(machineOrderService.findById(installPlan1.getOrderId()) == null){
//                return ResultGenerator.genFailResult("错误，根据该 orderId " + installPlan1.getOrderId() + " 找不到对应的 machineOrder ！");
//            }

            /**
             * 检查machine是否存在，并且和订单匹配
             */
//            if (installPlan1.getMachineId() == null) {
//                return ResultGenerator.genFailResult("错误，machineId 为 null！");
//            }  else if(machineService.findById(installPlan1.getMachineId()) == null){
//                return ResultGenerator.genFailResult("错误，根据该 machineId " + installPlan1.getMachineId() + " 找不到对应的 machine ！");
//            } else if( !isMachineInTheOrder(installPlan1.getMachineId(), installPlan1.getOrderId())) {
//                return ResultGenerator.genFailResult("错误， 该机器是不属于该订单");
//            }

            installPlan1.setUpdateDate(new Date());
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
    @PostMapping("/sendUnDeliveryInstallPlans")
    public Result sendUnDeliveryInstallPlans() {
        Result result = installPlanService.sendUnDeliveryInstallPlans();
        return result;
    }

    /**
     * 添加并立即发送（MQTT） 该排产计划 给该计划所在组的所有安装组长
     *
     */
    @PostMapping("/addAndsendInstallPlanNow")
    public Result addAndsendInstallPlanNow(String installPlan) {

        InstallPlan installPlan1 = JSON.parseObject(installPlan, InstallPlan.class);
        if (installPlan1 == null) {
            return ResultGenerator.genFailResult("installPlan 解析得到null");
        }
        installPlan1.setCreateDate(new Date());

        ServerToClientMsg msg = new ServerToClientMsg();
        String str = null;

        msg.setNameplate(machineService.findById(installPlan1.getMachineId()).getNameplate());
        msg.setOrderNum(machineOrderService.findById(installPlan1.getOrderId()).getOrderNum());
        msg.setType(ServerToClientMsg.MsgType.INSTALL_PLAN);
        msg.setCmtSend(installPlan1.getCmtSend());
        msg.setInstallDatePlan(installPlan1.getInstallDatePlan());

        mqttMessageHelper.sendToClient(Constant.S2C_INSTALL_PLAN + installPlan1.getInstallGroupId(), JSON.toJSONString(msg));
        str = "MQTT SEND topic: " + Constant.S2C_INSTALL_PLAN +  installPlan1.getInstallGroupId() + ", nameplate: " + msg.getNameplate();
        logger.info(str);

        /**
         * 即使没有安装组长，也安排排产
         */
        installPlan1.setSendTime(new Date());
        installPlanService.save(installPlan1);

        logger.info(str);
        return ResultGenerator.genSuccessResult(str);
    }
}
