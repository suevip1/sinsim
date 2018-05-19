package com.eservice.api.service.mqtt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * @author Wilson Hu  2017-12-01
 */
@Component
@Service
public class MqttMessageHelper {

    @Autowired
    MqttService.MyGateway myGateway;

    /**
     * 向MQTT发送数据
     * @param topic
     * @param msg
     */
    public void sendToClient(String topic, String msg){
        try {
            myGateway.sendToMqtt(topic, msg);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(MqttMessageHelper.class);
            logger.error("MQTT消息发送异常", e);
        }
    }

    /**
     * 用于接收MQTT数据，具体业务需要解析message对象后完成
     * @param message
     * @throws Exception
     */
    public void handleMessage(Message<?> message) throws Exception {
        String topic = message.getHeaders().get(MqttHeaders.TOPIC).toString();
        String payload = message.getPayload().toString();
        System.out.println("Topic:" + topic +" || Payload:" + payload);
    }
}
