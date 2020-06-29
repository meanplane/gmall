package com.mp.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.ware.Purchase;
import com.mp.common.utils.PageUtils;
import com.mp.ware.vo.MergeVo;
import com.mp.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 */
public interface PurchaseService extends IService<Purchase> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);


    void mergePurchase(MergeVo mergeVo);


    void received(List<Long> ids);


    void done(PurchaseDoneVo doneVo);


}

