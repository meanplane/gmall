package com.mp.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.coupon.SeckillSkuRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动商品关联
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-08 09:36:40
 */
@Mapper
public interface SeckillSkuRelationMapper extends BaseMapper<SeckillSkuRelation> {
	
}
