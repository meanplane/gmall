package com.mp.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.product.Attr;
import com.mp.common.utils.PageUtils;
import com.mp.product.vo.AttrGroupRelationVo;
import com.mp.product.vo.AttrRespVo;
import com.mp.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-19
 */
public interface AttrService extends IService<Attr> {
    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<Attr> getRelationAttr(Long attrgroupId);

    void deleteRelation(List<AttrGroupRelationVo> vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
}
