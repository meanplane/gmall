package com.mp.product.controller;

import com.mp.common.bean.product.Category;
import com.mp.common.utils.R;
import com.mp.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-11
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("/list/tree")
    public R listTree(){
        List<Category> categories = categoryService.listWithTree();
        return R.ok().put("data",categories);
    }
}
