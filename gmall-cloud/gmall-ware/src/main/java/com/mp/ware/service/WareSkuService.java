package com.mp.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.ware.WareSku;
import com.mp.common.utils.PageUtils;

import java.util.Map;

/**
 * 商品库存
 */
public interface WareSkuService extends IService<WareSku> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);


}

