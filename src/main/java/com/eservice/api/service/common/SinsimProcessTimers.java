package com.eservice.api.service.common;

import com.eservice.api.service.impl.WholeInstallPlanServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时检查是否发送排产计划
 */
@Component
public class SinsimProcessTimers {
    private final static Logger logger = LoggerFactory.getLogger(SinsimProcessTimers.class);

    @Resource
    private WholeInstallPlanServiceImpl wholeInstallPlanService;

    /**
     * 每分钟          0 0/1 * * * ?
     * 每天 10:00     0 00 10 * * ?
     * 每天检查是否要发送。
     */
    @Scheduled(cron = "0 00 10 * * ?")
    public void sendOnMorning() {
        logger.info("cron coming ： 0 00 10 * * ?  每天10点钟");

        /**
         * 发送所有未发送的总装排产计划
         */
        wholeInstallPlanService.sendUnDeliveryWIPs();
    }
}

