package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.SkuImages;
import com.mp.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
public interface SkuImagesService extends IService<SkuImages> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuImages> getImagesBySkuId(Long skuId);
}

