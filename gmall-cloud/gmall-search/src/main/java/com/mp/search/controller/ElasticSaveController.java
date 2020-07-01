package com.mp.search.controller;

import com.mp.common.bean.to.to.SkuEsModel;
import com.mp.common.utils.R;
import com.mp.search.service.ProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Author: Xiaoer
 * Date: 2020-07-01
 */
@RequestMapping("/save")
@RestController
public class ElasticSaveController {
    @Autowired
    private ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) throws IOException {
        productSaveService.productStatusUp(skuEsModels);
        return R.ok();
    }
}
