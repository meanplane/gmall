package com.mp.ware.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.ware.WareSku;
import com.mp.common.bean.ware.vo.SkuHasStockVo;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.common.utils.R;
import com.mp.ware.feign.ProductFeignService;
import com.mp.ware.mapper.WareSkuMapper;
import com.mp.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuMapper, WareSku> implements WareSkuService {

    @Autowired
    WareSkuMapper wareSkuMapper;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         * skuId: 1
         * wareId: 2
         */
        QueryWrapper<WareSku> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }

        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }


        IPage<WareSku> page = this.page(
                new Query<WareSku>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断如果还没有这个库存记录新增
        List<WareSku> entities = wareSkuMapper.selectList(new QueryWrapper<WareSku>().eq("sku_id", skuId).eq("ware_id", wareId));
        if(entities == null || entities.size() == 0){
            WareSku sku = new WareSku();
            sku.setSkuId(skuId);
            sku.setStock(skuNum);
            sku.setWareId(wareId);
            sku.setStockLocked(0);
            //TODO 远程查询sku的名字，如果失败，整个事务无需回滚
            //1、自己catch异常
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            try {
                R info = productFeignService.info(skuId);
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");

                if(info.getCode() == 0){
                    sku.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){

            }


            wareSkuMapper.insert(sku);
        }else{
            wareSkuMapper.addStock(skuId,wareId,skuNum);
        }

    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        // 查询库存不为0 的商品
        QueryWrapper<WareSku> q = new QueryWrapper<>();
        q.in("sku_id",skuIds);
        q.gt("stock",0);

        List<WareSku> hasList = baseMapper.selectList(q);
        List<SkuHasStockVo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            vo.setSkuId(skuId);
            vo.setHasStock(hasList.contains(skuId));
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

}
