package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.Category;

import java.util.List;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-10
 */
public interface CategoryService extends IService<Category> {

    public List<Category> listWithTree();

    public void updateCasecade(Category category);

    Long[] findCatelogPath(Long catelogId);
}
