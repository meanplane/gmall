package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.AttrAttrgroupRelation;
import com.mp.common.utils.PageUtils;
import com.mp.product.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-18
 */
public interface AttrAttrGroupRelationService extends IService<AttrAttrgroupRelation> {
    public PageUtils queryPage(Map<String, Object> params);
    public void saveBatch(List<AttrGroupRelationVo> vos);
}
