package com.mp.product.controller;

import com.mp.common.bean.product.Brand;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.R;
import com.mp.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-16
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @RequestMapping("/list")
    public R getList(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    @RequestMapping("/info/{bid}")
    public R info(@PathVariable("bid") Long bid) {
        Brand brand = brandService.getById(bid);
        return R.ok().put("brand", brand);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody Brand brand) {
        brandService.save(brand);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody Brand brand) {
        brandService.updateDetail(brand);
        return R.ok();
    }

    @RequestMapping("/update/status")
    public R updateStatus(@RequestBody Brand brand) {
        brandService.updateById(brand);
        return R.ok();
    }

}
