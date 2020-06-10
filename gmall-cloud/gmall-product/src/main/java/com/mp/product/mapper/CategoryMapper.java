package com.mp.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.product.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品三级分类
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:48
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    @Select("select * from pms_category limit 5")
    public List<Category> getAllCategorys();
}
