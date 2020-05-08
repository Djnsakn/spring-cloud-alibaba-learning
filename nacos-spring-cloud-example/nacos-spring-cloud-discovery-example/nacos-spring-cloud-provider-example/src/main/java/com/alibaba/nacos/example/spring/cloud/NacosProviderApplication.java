package com.alibaba.nacos.example.spring.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@EnableDiscoveryClient  // 通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
public class NacosProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosProviderApplication.class, args);
	}

	@Value("${server.port}")
	String port;

	@RestController
	class EchoController {
		@RequestMapping("/echo")
		public String echo(String name) {
			return "Hello Nacos Discovery " + name + " port:" + port;
		}
	}
}
