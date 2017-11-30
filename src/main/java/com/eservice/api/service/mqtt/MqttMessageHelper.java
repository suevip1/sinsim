package com.eservice.api.service.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Wilson Hu  2017-12-01
 */
@Component
public class MqttMessageHelper {


    @Autowired
    MqttService.MyGateway myGateway;

    public void sendToClient(String clientId, String msg){
        myGateway.sendToMqtt(msg, "topic/client/" + clientId);
    }

}
