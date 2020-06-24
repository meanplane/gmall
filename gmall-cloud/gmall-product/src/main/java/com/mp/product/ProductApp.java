package com.mp.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Author: Xiaoer
 * Date: 2020-06-10
 */
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class ProductApp {
    public static void main(String[] args) {
        SpringApplication.run(ProductApp.class,args);
    }
}
