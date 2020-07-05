package com.mp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.SkuSaleAttrValue;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.product.mapper.SkuSaleAttrValueMapper;
import com.mp.product.service.SkuSaleAttrValueService;
import com.mp.product.vo.SkuItemSaleAttrVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueMapper, SkuSaleAttrValue> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValue> page = this.page(
                new Query<SkuSaleAttrValue>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVO> getSaleAttrsBySpuId(Long spuId) {
        return this.baseMapper.getSaleAttrsBySpuId(spuId);
    }

    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {
        return this.baseMapper.getSkuSaleAttrValuesAsStringList(skuId);
    }

}
