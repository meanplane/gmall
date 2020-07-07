package com.mp.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.SkuImages;
import com.mp.common.bean.product.SkuInfo;
import com.mp.common.bean.product.SpuInfoDesc;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.common.utils.R;
import com.mp.product.mapper.SkuInfoMapper;
import com.mp.product.service.*;
import com.mp.product.vo.SeckillInfoVO;
import com.mp.product.vo.SkuItemSaleAttrVO;
import com.mp.product.vo.SkuItemVo;
import com.mp.product.vo.SpuItemAttrGroupVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {
    @Resource
    private SkuImagesService skuImagesService;

    @Resource
    private SpuInfoDescService spuInfoDescService;

    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

//    @Resource
//    private SeckillFeign seckillFeign;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfo> page = this.page(
                new Query<SkuInfo>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfo skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfo> queryWrapper = new QueryWrapper<>();
        /**
         * key:
         * categoryId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
               wrapper.eq("sku_id",key).or().like("sku_name",key);
            });
        }

        String categoryId = (String) params.get("categoryId");
        if(!StringUtils.isEmpty(categoryId)&&!"0".equalsIgnoreCase(categoryId)){

            queryWrapper.eq("category_id",categoryId);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(categoryId)){
            queryWrapper.eq("brand_id",brandId);
        }

        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)){
            queryWrapper.ge("price",min);
        }

        String max = (String) params.get("max");

        if(!StringUtils.isEmpty(max)  ){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);

                if(bigDecimal.compareTo(new BigDecimal("0"))==1){
                    queryWrapper.le("price",max);
                }
            }catch (Exception e){

            }

        }


        IPage<SkuInfo> page = this.page(
                new Query<SkuInfo>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfo> getSkusBySpuId(Long spuId) {
        QueryWrapper<SkuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id",spuId);

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVO = new SkuItemVo();

        // sku 基本信息
        CompletableFuture<SkuInfo> infoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfo info = getById(skuId);
            skuItemVO.setInfo(info);
            return info;
        }, threadPoolExecutor);

        // spu 的销售属性组合
        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync(res -> {
            List<SkuItemSaleAttrVO> saleAttrVOS = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVO.setSaleAttr(saleAttrVOS);
        }, threadPoolExecutor);

        // spu 介绍
        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync(res -> {
            SpuInfoDesc spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVO.setDesc(spuInfoDescEntity);
        }, threadPoolExecutor);

        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync(res -> {
            // spu 的规格参数信息
            List<SpuItemAttrGroupVO> attrGroupVOS = attrGroupService.getAttrGroupWithAttrsBySpuIdAndCategoryId(res.getSpuId(), res.getCategoryId());
            skuItemVO.setGroupAttrs(attrGroupVOS);
        }, threadPoolExecutor);

        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            // sku 图片信息
            List<SkuImages> images = skuImagesService.getImagesBySkuId(skuId);
            skuItemVO.setImages(images);
        }, threadPoolExecutor);

//        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
//            R skuSeckillInfo = seckillFeign.getSkuSeckillInfo(skuId);
//            if (skuSeckillInfo.getCode() == 0) {
//                SeckillInfoVO seckillInfoVO = skuSeckillInfo.getData(new TypeReference<SeckillInfoVO>() {
//                });
//                skuItemVO.setSeckillInfo(seckillInfoVO);
//            }
//        }, threadPoolExecutor);

//        CompletableFuture.allOf(saleAttrFuture, descFuture, baseAttrFuture, imageFuture, seckillFuture).get();
                CompletableFuture.allOf(saleAttrFuture, descFuture, baseAttrFuture, imageFuture).get();
        return skuItemVO;
    }

}
