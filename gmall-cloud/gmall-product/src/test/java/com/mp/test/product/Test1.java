package com.mp.test.product;

import com.mp.product.ProductApp;
import com.mp.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApp.class)
public class Test1 {

    @Autowired
    private BrandService brandService;

    @Test
    public void test1(){

    }
}
