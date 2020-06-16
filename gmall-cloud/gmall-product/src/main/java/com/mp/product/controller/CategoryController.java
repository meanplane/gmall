package com.mp.product.controller;

import com.mp.common.bean.product.Category;
import com.mp.common.utils.R;
import com.mp.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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
    public R listTree() {
        List<Category> categories = categoryService.listWithTree();
        return R.ok().put("data", categories);
    }

    @RequestMapping("/info/{cid}")
    public R getInfo(@PathVariable("cid") Long cid) {
        Category category = categoryService.getById(cid);
        return R.ok().put("data", category);
    }

    @RequestMapping("/save")
    public R saveCatagory(@RequestBody Category category) {
        categoryService.save(category);
        return R.ok();
    }

    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody Category[] categories) {
        categoryService.updateBatchById(Arrays.asList(categories));
        return R.ok();
    }

    @RequestMapping("/update")
    public R updateCategory(@RequestBody Category category) {
        categoryService.updateCasecade(category);
        return R.ok();
    }


    @RequestMapping("/delete")
    public R deleteList(@RequestBody Long[] ids) {
        categoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }


}
