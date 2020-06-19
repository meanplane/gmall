package com.mp.product.controller;

import com.mp.common.utils.R;
import com.mp.product.service.impl.AwsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-17
 */
@RestController
@RequestMapping("/aws")
public class AwsController {
    @Autowired
    private AwsServiceImpl awsService;

    @RequestMapping("/getsecret/{uid}")
    public R getSecret(@PathVariable("uid") String uid) throws Exception {
        Map<String, Object> stsToken = awsService.getStsToken(uid, 21600L);
        return R.ok(stsToken);
    }
}
