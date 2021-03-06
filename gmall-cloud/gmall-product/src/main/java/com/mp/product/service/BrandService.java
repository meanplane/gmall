package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.Brand;
import com.mp.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-16
 */
public interface BrandService extends IService<Brand> {

    public PageUtils queryPage(Map<String, Object> params);

    public void updateDetail(Brand brand);

    void removeCascade(List<Long> bids);
}
