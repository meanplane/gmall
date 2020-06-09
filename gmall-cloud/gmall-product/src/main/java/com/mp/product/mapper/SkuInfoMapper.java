package com.mp.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.product.SkuInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * sku信息
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
@Mapper
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {
	
}
