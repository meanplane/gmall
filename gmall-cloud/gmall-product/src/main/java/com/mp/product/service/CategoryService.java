package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.Category;
import com.mp.product.vo.Category2VO;

import java.util.List;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-10
 */
public interface CategoryService extends IService<Category> {

    public List<Category> listWithTree();

    public void updateCascade(Category category);

    List<Long> findCategoryPath(Long categoryId);

    void removeCascade(List<Long> asList);

    List<Category> getLevel1Categories();

    Map<String, List<Category2VO>> getCategoryJson() throws InterruptedException;
}
