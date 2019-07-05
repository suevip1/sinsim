package com.eservice.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.eservice.api.core.Result;
import com.eservice.api.core.ResultGenerator;
import com.eservice.api.dao.WholeInstallPlanMapper;
import com.eservice.api.model.whole_install_plan.WholeInstallPlan;
import com.eservice.api.service.MachineOrderService;
import com.eservice.api.service.WholeInstallPlanService;
import com.eservice.api.core.AbstractService;
import com.eservice.api.service.common.Constant;
import com.eservice.api.service.mqtt.MqttMessageHelper;
import com.eservice.api.service.mqtt.ServerToClientMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
* Class Description: xxx
* @author Wilson Hu
* @date 2019/06/22.
*/
@Service
@Transactional
public class WholeInstallPlanServiceImpl extends AbstractService<WholeInstallPlan> implements WholeInstallPlanService {
    @Resource
    private WholeInstallPlanMapper wholeInstallPlanMapper;

    private final static Logger logger = LoggerFactory.getLogger(WholeInstallPlanServiceImpl.class);
    @Resource
    private WholeInstallPlanServiceImpl wholeInstallPlanService;

    @Resource
    private MachineServiceImpl machineService;

    @Resource
    private MachineOrderService machineOrderService;

    @Resource
    private MqttMessageHelper mqttMessageHelper;

    public List<WholeInstallPlan> selectUnSendWIPs(){
       return wholeInstallPlanMapper.selectUnSendWIPs();
    }

    public Result sendUnDeliveryWIPs(){
        /**
         * 发送所有未发送的总装排产计划
         */
        ServerToClientMsg msg = new ServerToClientMsg();
        List<WholeInstallPlan> unSendWIPslist = wholeInstallPlanService.selectUnSendWIPs();

        for (int i = 0; i < unSendWIPslist.size(); i++) {
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = formatter.format(now);
            String installDatePlanDateString = formatter.format( unSendWIPslist.get(i).getInstallDatePlan());

            if(getAfterDay(nowDate,1).equals(installDatePlanDateString )) {
                logger.info( unSendWIPslist.get(i).getInstallDatePlan() + " 明天要安装：" +  machineService.findById(unSendWIPslist.get(i).getMachineId()).getNameplate());

                msg.setNameplate( machineService.findById(unSendWIPslist.get(i).getMachineId()).getNameplate());
                msg.setOrderNum( machineOrderService.findById(unSendWIPslist.get(i).getOrderId()).getOrderNum());
                msg.setType(ServerToClientMsg.MsgType.WHOLE_INSTALL_PLAN);
                msg.setCmtSend(unSendWIPslist.get(i).getCmtSend());
                msg.setInstallDatePlan(unSendWIPslist.get(i).getInstallDatePlan());

                mqttMessageHelper.sendToClient(Constant.S2C_WHOLE_INSTALL_PLAN, JSON.toJSONString(msg));
                logger.info("MQTT SEND topic: " + Constant.S2C_WHOLE_INSTALL_PLAN + ", nameplate: " + msg.getNameplate() );
            } else {
                logger.info("还未到安装提醒时间的机器：" +  machineService.findById(unSendWIPslist.get(i).getMachineId()).getNameplate());
            }

        }
        return ResultGenerator.genSuccessResult(unSendWIPslist.size() + " item(s) sent totally");
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
