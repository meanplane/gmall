package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.SkuSaleAttrValue;
import com.mp.common.utils.PageUtils;
import com.mp.product.vo.SkuItemSaleAttrVO;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuItemSaleAttrVO> getSaleAttrsBySpuId(Long spuId);

    List<String> getSkuSaleAttrValuesAsStringList(Long skuId);
}

