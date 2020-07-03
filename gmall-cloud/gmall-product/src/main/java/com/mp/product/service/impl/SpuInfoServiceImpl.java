package com.mp.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.*;
import com.mp.common.bean.to.to.SkuEsModel;
import com.mp.common.bean.ware.vo.SkuHasStockVo;
import com.mp.common.constant.ProductConst;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.common.utils.R;
import com.mp.product.feign.CouponFeignService;
import com.mp.product.feign.SearchFeignService;
import com.mp.product.feign.WareFeignService;
import com.mp.product.mapper.SpuInfoMapper;
import com.mp.product.service.*;
import com.mp.product.vo.spu.BaseAttrs;
import com.mp.product.vo.spu.Images;
import com.mp.product.vo.spu.Skus;
import com.mp.product.vo.spu.SpuSaveVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Xiaoer
 * Date: 2020-06-23
 */
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo> implements SpuInfoService {
    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SearchFeignService searchFeignService;

    @Autowired
    private WareFeignService wareFeignService;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfo> page = this.page(
                new Query<SpuInfo>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        //1、保存spu基本信息 pms_spu_info
        SpuInfo infoEntity = new SpuInfo();
        BeanUtils.copyProperties(vo,infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        //2、保存Spu的描述图片 pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDesc descEntity = new SpuInfoDesc();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);



        //3、保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(infoEntity.getId(),images);


        //4、保存spu的规格参数;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValue> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValue valueEntity = new ProductAttrValue();
            valueEntity.setAttrId(attr.getAttrId());
            Attr id = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(id.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());

            return valueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);


        //5、保存spu的积分信息；gulimall_sms->sms_spu_bounds
//        Bounds bounds = vo.getBounds();
//        SpuBoundTo spuBoundTo = new SpuBoundTo();
//        BeanUtils.copyProperties(bounds,spuBoundTo);
//        spuBoundTo.setSpuId(infoEntity.getId());
//        R r = couponFeignService.saveSpuBounds(spuBoundTo);
//        if(r.getCode() != 0){
//            log.error("远程保存spu积分信息失败");
//        }


        //5、保存当前spu对应的所有sku信息；

        List<Skus> skus = vo.getSkus();
        if(skus!=null && skus.size()>0){
            skus.forEach(item->{
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if(image.getDefaultImg() == 1){
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfo skuInfoEntity = new SkuInfo();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuName(item.getSkuName());
                skuInfoEntity.setCategoryId(infoEntity.getCategoryId());
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoEntity.setSkuTitle(item.getSkuTitle());
                skuInfoEntity.setSkuSubtitle(item.getSkuSubtitle());
                skuInfoEntity.setPrice(item.getPrice());
                skuInfoEntity.setSaleCount(0L);
                //5.1）、sku的基本信息；pms_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImages> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImages skuImagesEntity = new SkuImages();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->{
                    //返回true就是需要，false就是剔除
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //5.2）、sku的图片信息；pms_sku_image
                skuImagesService.saveBatch(imagesEntities);
                //TODO 没有图片路径的无需保存

                List<com.mp.product.vo.spu.Attr> attr = item.getAttr();
                List<SkuSaleAttrValue> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValue attrValueEntity = new SkuSaleAttrValue();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);

                    return attrValueEntity;
                }).collect(Collectors.toList());
                //5.3）、sku的销售属性信息：pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // //5.4）、sku的优惠、满减等信息；gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
//                SkuReductionTo skuReductionTo = new SkuReductionTo();
//                BeanUtils.copyProperties(item,skuReductionTo);
//                skuReductionTo.setSkuId(skuId);
//                if(skuReductionTo.getFullCount() >0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1){
//                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
//                    if(r1.getCode() != 0){
//                        log.error("远程保存sku优惠信息失败");
//                    }
//                }

            });
        }

    }

    @Override
    public void saveBaseSpuInfo(SpuInfo infoEntity) {
        this.baseMapper.insert(infoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        // status=1 and (id=1 or spu_name like xxx)
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }

        String categoryId = (String) params.get("categoryId");
        if(!StringUtils.isEmpty(categoryId)&&!"0".equalsIgnoreCase(categoryId)){
            wrapper.eq("category_id",categoryId);
        }

        /**
         * status: 2
         * key:
         * brandId: 9
         * categoryId: 225
         */

        IPage<SpuInfo> page = this.page(
                new Query<SpuInfo>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        // 1.查出当前spuid 对应的所有sku信息, 品牌的名字
        List<SkuInfo> skus = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdLists = skus.stream().map(SkuInfo::getSkuId).collect(Collectors.toList());

        // 4 查询当前 sku 的所有可以被用来检索的规格属性
        List<ProductAttrValue> baseAttrs = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> attr.getAttrId()).collect(Collectors.toList());

        List<Attr> attrs = attrService.selectSearchAttrs(attrIds);

        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> attrs.contains(item.getAttrId()))
                .map(item -> {
                    SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
                    BeanUtils.copyProperties(item, attrs1);
                    return attrs1;
                })
                .collect(Collectors.toList());

        // todo 发送远程调用 在仓库中查询是否有库存
        Map<Long, Boolean> stockMap = new HashMap<>();
        try {
            R r = wareFeignService.getSkusHasStock(skuIdLists);
            Object data = r.get("data");

            List<SkuHasStockVo> lists = JSON.parseArray((String) data, SkuHasStockVo.class);
            lists.stream().forEach(item -> stockMap.put(item.getSkuId(),item.getHasStock()));

//            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {};
//            stockMap = r.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
        } catch (Exception e) {
            log.error("库存服务查询异常，原因：", e);
        }


        // 2.封装每个sku信息
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku,skuEsModel);

            //skuPrice,skuImg,
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());

            // todo 设置库存信息
            if (stockMap == null) {
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(stockMap.get(sku.getSkuId()));
            }
            // todo 2 发送远程调用,热度评分
            skuEsModel.setHotScore(0L);

            // todo 3 查询品牌和分类的名字
            Category category = categoryService.getById(sku.getCategoryId());
            skuEsModel.setCategoryName(category.getName());

            Brand brand = brandService.getById(sku.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());

            // 查询当前sku的所有规格属性
            skuEsModel.setAttrs(attrsList);


            return skuEsModel;
        }).collect(Collectors.toList());

        // 5 将数据发送给es保存
        R r = searchFeignService.productStatusUp(upProducts);
        if(r.getCode() == 0){
            // 远程调用成功
            // todo 6 修改当前spu的状态
            baseMapper.updateSpuStatus(spuId, ProductConst.StatusEnum.SPU_UP.getCode());
        }else{
            // 调用失败
            // todo 7 重复调用 ? 接口幂等性
        }
    }
}


