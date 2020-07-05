package com.mp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.SkuImages;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.product.mapper.SkuImagesMapper;
import com.mp.product.service.SkuImagesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesMapper, SkuImages> implements SkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuImages> page = this.page(
                new Query<SkuImages>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuImages> getImagesBySkuId(Long skuId) {
        return this.baseMapper.selectList(new QueryWrapper<SkuImages>().eq("sku_id", skuId));
    }
}
