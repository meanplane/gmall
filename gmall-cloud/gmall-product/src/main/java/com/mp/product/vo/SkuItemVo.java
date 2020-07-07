package com.mp.product.vo;

import com.mp.common.bean.product.SkuImages;
import com.mp.common.bean.product.SkuInfo;
import com.mp.common.bean.product.SpuInfoDesc;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {

    private SkuInfo info;

    private boolean hasStock = true;

    private List<SkuImages> images;

    private List<SkuItemSaleAttrVO> saleAttr;

    private SpuInfoDesc desc;

    private List<SpuItemAttrGroupVO> groupAttrs;

    private SeckillInfoVO seckillInfo;

}

