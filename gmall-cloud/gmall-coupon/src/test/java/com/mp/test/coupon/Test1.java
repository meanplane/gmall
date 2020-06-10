package com.mp.test.coupon;

import com.mp.common.bean.coupon.Coupon;
import com.mp.coupon.CouponApp;
import com.mp.coupon.mapper.CouponMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-09
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CouponApp.class)
public class Test1 {
    @Autowired
    private CouponMapper couponMapper;

    @Test
    public void test1(){
        List<Coupon> coupons = couponMapper.selectList(null);
        coupons.forEach(item -> {
            System.out.println(item);
        });
    }
}
