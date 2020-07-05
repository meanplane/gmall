package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.AttrGroup;
import com.mp.common.utils.PageUtils;
import com.mp.product.vo.AttrGroupWithAttrsVo;
import com.mp.product.vo.SpuItemAttrGroupVO;

import java.util.List;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-18
 */
public interface AttrGroupService extends IService<AttrGroup> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long categoryId);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long categoryId);

    List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuIdAndCatalogId(Long spuId, Long catalogId);
}
