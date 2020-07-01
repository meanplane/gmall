package com.mp.test.product;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.mp.common.bean.product.Attr;
import com.mp.product.ProductApp;
import com.mp.product.service.AttrService;
import com.mp.product.service.BrandService;
import com.mp.product.service.CategoryBrandRelationService;
import com.mp.product.service.impl.AwsServiceImpl;
import com.mp.product.utils.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApp.class)
public class Test1 {

    @Autowired
    private BrandService brandService;

    @Autowired
    private AwsServiceImpl awsService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private AttrService attrService;

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public static final String DEFAULT_CHARSET = "UTF-8";

    @Test
    public void testRedis() throws Exception {
        Map<String, Object> stsToken = awsService.getStsToken("1000010", 21600L);
        System.out.println(stsToken);
    }

    @Test
    public void testRedis1() {
        Object awskey = redisUtils.get("awskey");
        System.out.println(awskey);
    }

    @Test
    public void testSql() {
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(2L);
        ids.add(3L);
        ids.add(5L);
        ids.add(6L);
        List<Attr> attrs = attrService.selectSearchAttrs(ids);
        for (Attr attr : attrs) {
            System.out.println(attr);
        }
    }
}
