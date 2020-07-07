package com.mp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.Attr;
import com.mp.common.bean.product.AttrGroup;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.product.mapper.AttrGroupMapper;
import com.mp.product.service.AttrGroupService;
import com.mp.product.service.AttrService;
import com.mp.product.vo.AttrGroupWithAttrsVo;
import com.mp.product.vo.SpuItemAttrGroupVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Xiaoer
 * Date: 2020-06-18
 */
@Service
@Slf4j
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup> implements AttrGroupService {
    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroup> page = this.page(
                new Query<AttrGroup>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        String key = (String) params.get("key");
        //select * from pms_attr_group where category_id=? and (attr_group_id=key or attr_group_name like %key%)
        QueryWrapper<AttrGroup> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(obj -> obj.eq("attr_group_id", key).or().like("attr_group_name", key));
        }

        IPage<AttrGroup> page;

        if (categoryId != 0) {
            wrapper.eq("category_id", categoryId);
        }
        page = this.page(new Query<AttrGroup>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }

    /**
     * 根据分类id查出所有的分组以及这些组里面的属性
     * @param categoryId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long categoryId) {
        //1、查询分组信息
        List<AttrGroup> attrGroupEntities = this.list(new QueryWrapper<AttrGroup>().eq("category_id", categoryId));

        //2、查询所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group,attrsVo);
            List<Attr> attrs = attrService.getRelationAttr(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(attrs);
            return attrsVo;
        }).collect(Collectors.toList());

        return collect;


    }

    @Override
    public List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuIdAndCategoryId(Long spuId, Long categoryId) {
        return this.baseMapper.getAttrGroupWithAttrsBySpuIdAndCategoryId(spuId, categoryId);
    }
}
