package com.mp.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.Category;
import com.mp.product.mapper.CategoryMapper;
import com.mp.product.service.CategoryBrandRelationService;
import com.mp.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Xiaoer
 * Date: 2020-06-10
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public List<Category> listWithTree() {
        List<Category> categories = baseMapper.selectList(null);
        List<Category> collect = categories.stream()
                .filter((menu) -> {
                    return menu.getParentCid() != null && menu.getParentCid() == 0;

                }).map((category) -> {
                    category.setChildren(getChildrens(category.getCatId(), categories));
                    return category;

                }).sorted((menu1, menu2) -> {
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());
        return collect;
    }

    private List<Category> getChildrens(Long rootId, List<Category> all) {
        List<Category> children = all.stream()
                .filter((menu) -> {
                    return menu != null && menu.getParentCid().equals(rootId);

                }).map((category) -> {
                    category.setChildren(getChildrens(category.getCatId(), all));
                    return category;

                }).sorted((menu1, menu2) -> {
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());

        return children;
    }

    // 级联更新所有关联的数据
    @Transactional
    @Override
    public void updateCascade(Category category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    // [2,25,225]
    @Override
    public List<Long> findCategoryPath(Long categoryId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(categoryId, paths);
        Collections.reverse(parentPath);

        return parentPath;
    }

    @Transactional
    @Override
    public void removeCascade(List<Long> asList) {
        this.removeByIds(asList);
        categoryBrandRelationService.deleteCategory(asList);
    }

    //225,25,2
    private List<Long> findParentPath(Long categoryId,List<Long> paths){
        //1、收集当前节点id
        paths.add(categoryId);
        Category byId = this.getById(categoryId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;

    }

}
