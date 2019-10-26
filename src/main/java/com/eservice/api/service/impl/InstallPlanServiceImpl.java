package com.eservice.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.dao.InstallPlanMapper;
import com.eservice.api.model.install_plan.InstallPlan;
import com.eservice.api.model.user.User;
import com.eservice.api.model.user.UserDetail;
import com.eservice.api.service.InstallPlanService;
import com.eservice.api.core.AbstractService;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.mqtt.MqttMessageHelper;
import com.eservice.api.service.mqtt.ServerToClientMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/07/19.
*/
@Service
@Transactional
public class InstallPlanServiceImpl extends AbstractService<InstallPlan> implements InstallPlanService {
    @Resource
    private InstallPlanMapper installPlanMapper;
    @Resource
    private InstallPlanServiceImpl installPlanService;

    @Resource
    private MachineServiceImpl machineService;

    @Resource
    private MachineOrderService machineOrderService;

    @Resource
    private MqttMessageHelper mqttMessageHelper;

    @Resource
    private UserServiceImpl userService;
    private final static Logger logger = LoggerFactory.getLogger(InstallPlanServiceImpl.class);
    public List<InstallPlan> selectUnSendInstallPlans(){
        return installPlanMapper.selectUnSendInstallPlans();
    }

    public Result sendUnDeliveryInstallPlans() {
        /**
         * 通过MQTT 发送所有未发送的排产计划
         */
        ServerToClientMsg msg = new ServerToClientMsg();
        List<InstallPlan> unSendInstallPlans = installPlanService.selectUnSendInstallPlans();
        int sendCount = 0;
        for (int i = 0; i < unSendInstallPlans.size(); i++) {
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = formatter.format(now);
            String installDatePlanDateString = formatter.format(unSendInstallPlans.get(i).getInstallDatePlan());

            if (getAfterDay(nowDate, 1).equals(installDatePlanDateString)) {
                logger.info(unSendInstallPlans.get(i).getInstallDatePlan() + " 明天要安装：" + machineService.findById(unSendInstallPlans.get(i).getMachineId()).getNameplate());

                msg.setNameplate(machineService.findById(unSendInstallPlans.get(i).getMachineId()).getNameplate());
                msg.setOrderNum(machineOrderService.findById(unSendInstallPlans.get(i).getOrderId()).getOrderNum());
                msg.setType(ServerToClientMsg.MsgType.INSTALL_PLAN);
                msg.setCmtSend(unSendInstallPlans.get(i).getCmtSend());
                msg.setInstallDatePlan(unSendInstallPlans.get(i).getInstallDatePlan());

                //topic结尾加组长的账号，并设置发送时间表示已发送。
                List<UserDetail> userDetailList = userService.selectUsers(null,null,
                        3,unSendInstallPlans.get(i).getInstallGroupId(),1);
                for(int k=0; k< userDetailList.size(); k++) {
                    mqttMessageHelper.sendToClient(Constant.S2C_INSTALL_PLAN + userDetailList.get(k).getAccount(), JSON.toJSONString(msg));
                    logger.info("MQTT SEND topic: " + Constant.S2C_INSTALL_PLAN +  userDetailList.get(k).getAccount() + ", nameplate: " + msg.getNameplate());
                    sendCount++;
                    unSendInstallPlans.get(i).setSendTime(new Date());
                    installPlanService.update(unSendInstallPlans.get(i));
                }
            } else {
                logger.info("还未到安装提醒时间的机器："
                        + machineService.findById(unSendInstallPlans.get(i).getMachineId()).getNameplate()
                        + " 安装时间为 " + installDatePlanDateString);
            }

        }
        String str = "还未到安装提醒时间的任务: " + unSendInstallPlans.size() + "条，"
                + "刚刚发送提醒 " + sendCount + "条";
        logger.info(str);
        return ResultGenerator.genSuccessResult(str);
    }

    /**
     * 返回取指定日期加n天， 比如今天加1天 就是返回明天
     * @param specifiedDay
     * @param n
     * @return
     */
    public static String getAfterDay(String specifiedDay,int n)
    {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + n);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        logger.info( n + " day after " + specifiedDay + " is " + dayAfter);
        return dayAfter;
    }
}
