package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.SpuInfo;
import com.mp.common.utils.PageUtils;
import com.mp.product.vo.spu.SpuSaveVo;

import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-23
 */
public interface SpuInfoService extends IService<SpuInfo> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfo infoEntity);


    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId);
}

