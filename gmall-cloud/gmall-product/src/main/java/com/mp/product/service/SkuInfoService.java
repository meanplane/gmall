package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.SkuInfo;
import com.mp.common.utils.PageUtils;

import java.util.Map;

/**
 * sku信息
 *
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
public interface SkuInfoService extends IService<SkuInfo> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfo skuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);


}

