package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.Brand;
import com.mp.common.bean.product.CategoryBrandRelation;
import com.mp.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelation> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelation categoryBrandRelation);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);

    List<Brand> getBrandsByCatId(Long catId);

    void deleteCategory(List<Long> ids);

    void deleteBrand(List<Long> bids);
}

