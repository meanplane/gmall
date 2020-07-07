package com.mp.product.web;

import com.mp.common.bean.product.Category;
import com.mp.product.service.CategoryService;
import com.mp.product.vo.Category2VO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-07-03
 */
@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        // TODO 1 查出所有的1级分类
        List<Category> categoryEntities = categoryService.getLevel1Categories();
        // 视图解析器进行拼串
        // classpath:/templates/ + result + .html
        model.addAttribute("categories", categoryEntities);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/category.json")
    public Map<String, List<Category2VO>> getCategoryJson() throws InterruptedException {
        return categoryService.getCategoryJson();
    }
}
