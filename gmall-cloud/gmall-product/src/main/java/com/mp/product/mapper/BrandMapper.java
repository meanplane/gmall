package com.mp.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.product.Brand;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
@Mapper
public interface BrandMapper extends BaseMapper<Brand> {
	
}
