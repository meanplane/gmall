package com.mp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.Attr;
import com.mp.common.bean.product.AttrAttrgroupRelation;
import com.mp.common.bean.product.AttrGroup;
import com.mp.common.bean.product.Category;
import com.mp.common.constant.ProductConst;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.product.mapper.AttrAttrgroupRelationMapper;
import com.mp.product.mapper.AttrGroupMapper;
import com.mp.product.mapper.AttrMapper;
import com.mp.product.mapper.CategoryMapper;
import com.mp.product.service.AttrService;
import com.mp.product.vo.AttrGroupRelationVo;
import com.mp.product.vo.AttrRespVo;
import com.mp.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Xiaoer
 * Date: 2020-06-19
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationMapper attrAttrgroupRelationMapper;

    @Autowired
    private AttrGroupMapper attrGroupMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Attr> page = this.page(
                new Query<Attr>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId, String type) {
        int code = "base".equalsIgnoreCase(type)? ProductConst.AttrEnum.ATTR_TYPE_BASE.getCode(): ProductConst.AttrEnum.ATTR_TYPE_SALE.getCode();
        QueryWrapper<Attr> queryWrapper = new QueryWrapper<Attr>().eq("attr_type",code);

        if (categoryId != 0){
            queryWrapper.eq("category_id",categoryId);
        }

        String key = (String)params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and(wrapper ->{
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<Attr> page = this.page(new Query<Attr>().getPage(params), queryWrapper);

        PageUtils pageUtils = new PageUtils(page);
        List<Attr> records = page.getRecords();
        List<Object> respVos = records.stream().map(attr -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attr,attrRespVo);

            //1、设置分类和分组的名字
            if ("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelation attrId = attrAttrgroupRelationMapper.selectOne(new QueryWrapper<AttrAttrgroupRelation>().eq("attr_id", attr.getAttrId()));
                if (attrId != null && attrId.getAttrGroupId() != null){
                    AttrGroup attrGroup = attrGroupMapper.selectById(attrId.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroup.getAttrGroupName());
                }
            }

            Category category = categoryMapper.selectById(attr.getCategoryId());
            if (category != null){
                attrRespVo.setCategoryName(category.getName());
            }
            return attrRespVo;

        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        return null;
    }

    /**
     * 根据分组id查找关联的所有基本属性
     */
    @Override
    public List<Attr> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelation> relations = attrAttrgroupRelationMapper.selectList(
                new QueryWrapper<AttrAttrgroupRelation>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = relations.stream().map(attr -> attr.getAttrId()).collect(Collectors.toList());

        if (attrIds == null || attrIds.size() == 0) {
            return null;
        }
        return this.listByIds(attrIds);
    }

    /**
     * 获取当前分组没有关联的所有属性
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        return null;
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {

    }

    @Override
    public void updateAttr(AttrVo attr) {

    }

    @Override
    public void saveAttr(AttrVo attr) {

    }
}
