package com.mp.test;

import com.mp.common.bean.ware.vo.SkuHasStockVo;
import com.mp.ware.WareApp;
import com.mp.ware.service.WareSkuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Xiaoer
 * Date: 2020-07-03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WareApp.class)
public class Test1 {

    @Autowired
    private WareSkuService wareSkuService;

    @Test
    public void test1(){
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(1L);
        longs.add(2L);
        longs.add(3L);
        longs.add(4L);
        List<SkuHasStockVo> skuHasStock = wareSkuService.getSkuHasStock(longs);
        System.out.println(skuHasStock);
    }
}
