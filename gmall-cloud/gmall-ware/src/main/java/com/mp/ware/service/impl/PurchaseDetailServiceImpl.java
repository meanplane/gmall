package com.mp.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.ware.PurchaseDetail;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.ware.mapper.PurchaseDetailMapper;
import com.mp.ware.service.PurchaseDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailMapper, PurchaseDetail> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        /**
         * status: 0,//状态
         *    wareId: 1,//仓库id
         */

        QueryWrapper<PurchaseDetail> queryWrapper = new QueryWrapper<PurchaseDetail>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            //purchase_id  sku_id
            queryWrapper.and(w->{
                w.eq("purchase_id",key).or().eq("sku_id",key);
            });
        }

        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            //purchase_id  sku_id
            queryWrapper.eq("status",status);
        }

        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            //purchase_id  sku_id
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<PurchaseDetail> page = this.page(
                new Query<PurchaseDetail>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetail> listDetailByPurchaseId(Long id) {

        List<PurchaseDetail> purchaseId = this.list(new QueryWrapper<PurchaseDetail>().eq("purchase_id", id));

        return purchaseId;
    }

}
