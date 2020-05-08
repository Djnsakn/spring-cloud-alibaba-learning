package com.alibaba.nacos.example.spring.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients  // 使用Feign
@EnableDiscoveryClient  // 通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
public class NacosConsumerApplication {

    @LoadBalanced  // 给 RestTemplate 实例添加 @LoadBalanced 注解，开启 @LoadBalanced 与 Ribbon 的集成
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args);
    }

    // 使用过RestTemplate来向服务的某个具体实例发起HTTP请求
    @RestController
    public class TestController {

        private final RestTemplate restTemplate;

        @Autowired
        public TestController(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

        @RequestMapping(value = "/echo", method = RequestMethod.GET)
        public String echo(String name) {
            return restTemplate.getForObject("http://service-provider/echo?name=" + name, String.class);
        }
    }


    // 使用Feign
    @RestController
    static class TestControllerFeign {

        @Autowired
        Client client;

        @GetMapping("/test")
        public String test(String name) {
            return client.hello(name);
        }
    }


    @FeignClient("service-provider")
    interface Client {

        @RequestMapping(value = "/echo", method = RequestMethod.GET)
        String hello(@RequestParam(name = "name") String name);

    }
}
