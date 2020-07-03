package com.mp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.Category;
import com.mp.product.mapper.CategoryMapper;
import com.mp.product.service.CategoryBrandRelationService;
import com.mp.product.service.CategoryService;
import com.mp.product.vo.Catelog2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @Override
    public List<Category> getLevel1Categories() {
        return baseMapper.selectList(new QueryWrapper<Category>().eq("parent_cid", 0));
    }

    @Override
    public Map<String, List<Catelog2VO>> getCatalogJson() throws InterruptedException {
        List<Category> selectList = baseMapper.selectList(null);
        List<Category> level1Categories = getParent_cid(selectList, 0L);

        Map<String, List<Catelog2VO>> parentCid = level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            List<Category> categoryEntities = getParent_cid(selectList, v.getCatId());
            List<Catelog2VO> catelog2VOS = null;
            if (!CollectionUtils.isEmpty(categoryEntities)) {
                catelog2VOS = categoryEntities.stream().map(l2 -> {
                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    List<Category> level3Catalog = getParent_cid(selectList, l2.getCatId());
                    if (!CollectionUtils.isEmpty(level3Catalog)) {
                        List<Catelog2VO.Catelog3VO> collect = level3Catalog.stream().map(l3 -> {
                            Catelog2VO.Catelog3VO catelog3VO = new Catelog2VO.Catelog3VO(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3VO;
                        }).collect(Collectors.toList());
                        catelog2VO.setCatalog3List(collect);
                    }
                    return catelog2VO;
                }).collect(Collectors.toList());
            }
            return catelog2VOS;
        }));
        return parentCid;
    }

    private List<Category> getParent_cid(List<Category> selectList, Long parentCid) {
        return selectList.stream().filter(o -> o.getParentCid().equals(parentCid)).collect(Collectors.toList());
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
