package com.mp.search.controller;

import com.mp.search.service.MallSearchService;
import com.mp.search.vo.SearchParam;
import com.mp.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * Author: Xiaoer
 * Date: 2020-07-06
 */
@Controller
public class SearchController {
    @Resource
    private MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model) {
        SearchResult result = mallSearchService.search(searchParam);
        model.addAttribute("result", result);
        return "list";
    }

}
