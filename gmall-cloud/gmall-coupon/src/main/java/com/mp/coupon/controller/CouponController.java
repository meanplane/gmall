package com.mp.coupon.controller;

import com.mp.common.bean.coupon.Coupon;
import com.mp.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-10
 */
@RefreshScope
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {

    @Value("${coupon.tom.name:xxx}")
    private String name;
    @Value("${coupon.tom.age:12}")
    private Integer age;

    @RequestMapping("/member/list")
    public R membercoupons(){
        Coupon coupon = new Coupon();
        coupon.setCouponName("满100减80");
        return R.ok().put("coupons", Arrays.asList(coupon));
    }

    @RequestMapping("/test")
    public R testConfig(){
        return R.ok().put("name",name).put("age",age);
    }
}
