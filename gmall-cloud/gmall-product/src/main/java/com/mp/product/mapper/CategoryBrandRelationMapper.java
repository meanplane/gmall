package com.mp.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.product.CategoryBrandRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌分类关联
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-11-17 21:25:25
 */
@Mapper
public interface CategoryBrandRelationMapper extends BaseMapper<CategoryBrandRelation> {

    void updateCategory(@Param("catId") Long catId, @Param("name") String name);

}
