package com.mp.test.product;

import com.mp.common.bean.product.Category;
import com.mp.product.ProductApp;
import com.mp.product.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApp.class)
public class Test1 {
//    @Autowired
//    CategoryMapper categoryMapper;

    @Autowired
    CategoryService categoryService;

    @Test
    public void testMapper(){
        List<Category> categories = categoryService.listWithTree();
        for (Category category : categories) {
            System.out.println(category);
        }
    }
}
