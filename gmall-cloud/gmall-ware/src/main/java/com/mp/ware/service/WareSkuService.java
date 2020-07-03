package com.mp.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.ware.WareSku;
import com.mp.common.bean.ware.vo.SkuHasStockVo;
import com.mp.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 */
public interface WareSkuService extends IService<WareSku> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

//    Boolean orderLockStock(WareSkuLockVO vo);

//    void unlockStock(StockLockedTO to);

//    void unlockStock(OrderTO orderTO);
}

