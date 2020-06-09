package com.mp.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.coupon.SkuFullReduction;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品满减信息
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-08 09:36:40
 */
@Mapper
public interface SkuFullReductionMapper extends BaseMapper<SkuFullReduction> {
	
}
