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
    @Override
    public void updateCasecade(Category category) {
        this.updateById(category);
        //categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

}
