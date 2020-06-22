package com.mp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.Brand;
import com.mp.common.bean.product.Category;
import com.mp.common.bean.product.CategoryBrandRelation;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.product.mapper.BrandMapper;
import com.mp.product.mapper.CategoryBrandRelationMapper;
import com.mp.product.mapper.CategoryMapper;
import com.mp.product.service.BrandService;
import com.mp.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationMapper, CategoryBrandRelation> implements CategoryBrandRelationService {

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CategoryBrandRelationMapper relationMapper;

    @Autowired
    BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelation> page = this.page(
                new Query<CategoryBrandRelation>().getPage(params),
                new QueryWrapper<CategoryBrandRelation>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelation categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long categoryId = categoryBrandRelation.getCategoryId();
        //1、查询详细名字
        Brand brandEntity = brandMapper.selectById(brandId);
        Category categoryEntity = categoryMapper.selectById(categoryId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCategoryName(categoryEntity.getName());

        this.save(categoryBrandRelation);

    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelation relationEntity = new CategoryBrandRelation();
        relationEntity.setBrandId(brandId);
        relationEntity.setBrandName(name);
        this.update(relationEntity, new UpdateWrapper<CategoryBrandRelation>().eq("brand_id", brandId));
    }

    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelation relationEntity = new CategoryBrandRelation();
        relationEntity.setCategoryId(catId);
        relationEntity.setCategoryName(name);
        this.update(relationEntity, new UpdateWrapper<CategoryBrandRelation>().eq("category_id", catId));
    }

    @Override
    public List<Brand> getBrandsByCatId(Long catId) {

        List<Long> brandIds = relationMapper.selectList(new QueryWrapper<CategoryBrandRelation>().eq("category_id", catId))
                .stream().map(item -> item.getBrandId()).collect(Collectors.toList());
        // todo: 如果brandIds 数量比较多,可以分组多查询几次
        return brandService.listByIds(brandIds);
    }

    @Override
    public void deleteCategory(List<Long> ids) {
        this.remove(new QueryWrapper<CategoryBrandRelation>().in("category_id", ids));
    }

    @Override
    public void deleteBrand(List<Long> bids) {
        this.remove(new QueryWrapper<CategoryBrandRelation>().in("brand_id", bids));
    }

}
