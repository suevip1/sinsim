package com.eservice.api;

import com.eservice.api.service.common.CommonService;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class Description: Application启动类
 *
 * @author Wilson Hu
 * @date 2017-10-25
 */

@SpringBootApplication
public class ApiApplication {

	static Logger logger = Logger.getLogger(ApiApplication.class);

	private static CommonService commonService;
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
		logger.info("get Arguments: " + String.join(", ", args));

		for(int i=0; i<args.length; i++){
			//--docker_gw_ip=172.21.0.1
			/**
			 * 来自dockerFile的IP docker_gw_ip
			 * 用于调用售后系统的接口、
			 * 但是无法传给commonService那边，所以不用传参数的方式
			 * 改为去固定目录下读取docker的Gateway的IP
			 */
//			if(args[i].substring(2,14).equals("docker_gw_ip")){
//				logger.info("Get docker_gw_ip: " + args[i].substring(15));
//				commonService.gatewayIpFromDockerFile =  args[i].substring(15);
//			}
		}
	}
}
