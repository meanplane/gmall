package com.mp.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.ware.PurchaseDetail;
import com.mp.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-11-17 13:50:10
 */
public interface PurchaseDetailService extends IService<PurchaseDetail> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetail> listDetailByPurchaseId(Long id);


}

