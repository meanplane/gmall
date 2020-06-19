package com.mp.product.controller;

import com.mp.common.utils.PageUtils;
import com.mp.common.utils.R;
import com.mp.product.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-19
 */
@RestController
@RequestMapping("/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    //product/attr/sale/list/0?
    ///product/attr/base/list/{categoryId}
    @GetMapping("/{attrType}/list/{categoryId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("categoryId") Long categoryId,
                          @PathVariable("attrType")String type){

        PageUtils page = attrService.queryBaseAttrPage(params,categoryId,type);
        return R.ok().put("page", page);
    }

}
