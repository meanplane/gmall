package com.mp.member.controller;

import com.mp.common.bean.member.Member;
import com.mp.common.utils.R;
import com.mp.member.feign.CouponFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Xiaoer
 * Date: 2020-06-10
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public R test(){
        Member member = new Member();
        member.setNickname("张三");

        R membercoupons = couponFeignService.membercoupons();
        return R.ok().put("member",member).put("coupons",membercoupons.get("coupons"));
    }
}
