package com.mp.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Author: Xiaoer
 * Date: 2020-06-09
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.mp.coupon.mapper")
public class CouponApp {
    public static void main(String[] args) {
        SpringApplication.run(CouponApp.class,args);
    }
}
