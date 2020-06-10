package com.mp.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.Category;
import com.mp.product.mapper.CategoryMapper;
import com.mp.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-10
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public List<Category> listWithTree() {
        List<Category> categories = baseMapper.selectList(null);
        List<Category> collect = categories.stream()
                .filter(category -> category.getParentCid() == 0)
                .collect(Collectors.toList());

        return collect;
    }

    private List<Category> getChildrens(Category root,List<Category> all){
        
    }
}
