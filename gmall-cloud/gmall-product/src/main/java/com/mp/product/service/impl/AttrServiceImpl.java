package com.mp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.mp.product.service.CategoryService;
import com.mp.product.vo.AttrGroupRelationVo;
import com.mp.product.vo.AttrRespVo;
import com.mp.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private CategoryService categoryService;

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
        int code = "base".equalsIgnoreCase(type) ? ProductConst.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConst.AttrEnum.ATTR_TYPE_SALE.getCode();
        QueryWrapper<Attr> queryWrapper = new QueryWrapper<Attr>().eq("attr_type", code);

        if (categoryId != 0) {
            queryWrapper.eq("category_id", categoryId);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<Attr> page = this.page(new Query<Attr>().getPage(params), queryWrapper);

        PageUtils pageUtils = new PageUtils(page);
        List<Attr> records = page.getRecords();
        List<Object> respVos = records.stream().map(attr -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attr, attrRespVo);

            //1、设置分类和分组的名字
            if ("base".equalsIgnoreCase(type)) {
                AttrAttrgroupRelation attrId = attrAttrgroupRelationMapper.selectOne(
                        new QueryWrapper<AttrAttrgroupRelation>().eq("attr_id", attr.getAttrId())
                );
                if (attrId != null && attrId.getAttrGroupId() != null) {
                    AttrGroup attrGroup = attrGroupMapper.selectById(attrId.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroup.getAttrGroupName());
                }
            }

            Category category = categoryMapper.selectById(attr.getCategoryId());
            if (category != null) {
                attrRespVo.setCategoryName(category.getName());
            }
            return attrRespVo;

        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo respVo = new AttrRespVo();
        Attr attr = this.getById(attrId);
        BeanUtils.copyProperties(attr, respVo);


        if (attr.getAttrType() == ProductConst.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //1、设置分组信息
            AttrAttrgroupRelation attrgroupRelation = attrAttrgroupRelationMapper.selectOne(new QueryWrapper<AttrAttrgroupRelation>().eq("attr_id", attrId));
            if (attrgroupRelation != null) {
                respVo.setAttrGroupId(attrgroupRelation.getAttrGroupId());
                AttrGroup attrGroupEntity = attrGroupMapper.selectById(attrgroupRelation.getAttrGroupId());
                if (attrGroupEntity != null) {
                    respVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }


        //2、设置分类信息
        Long categoryId = attr.getCategoryId();
        List<Long> categoryPath = categoryService.findCategoryPath(categoryId);
        respVo.setCategoryPath(categoryPath);

        Category categoryEntity = categoryMapper.selectById(categoryId);
        if (categoryEntity != null) {
            respVo.setCategoryName(categoryEntity.getName());
        }


        return respVo;
    }

    /**
     * 根据分组id查找关联的所有基本属性
     */
    @Override
    public List<Attr> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelation> relations = attrAttrgroupRelationMapper.selectList(
                new QueryWrapper<AttrAttrgroupRelation>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = relations.stream().map(attr -> attr.getAttrId()).collect(Collectors.toList());

        if (attrIds.size() == 0) {
            return null;
        }
        return this.listByIds(attrIds);
    }

    /**
     * 获取当前分组没有关联的所有属性
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroup attrGroupEntity = attrGroupMapper.selectById(attrgroupId);
        Long categoryId = attrGroupEntity.getCategoryId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1)、当前分类下的其他分组
        List<AttrGroup> group = attrGroupMapper.selectList(new QueryWrapper<AttrGroup>().eq("category_id", categoryId));
        List<Long> collect = group.stream().map(item -> item.getAttrGroupId()).collect(Collectors.toList());

        //2.2)、这些分组关联的属性
        List<AttrAttrgroupRelation> groupId = attrAttrgroupRelationMapper.selectList(new QueryWrapper<AttrAttrgroupRelation>().in("attr_group_id", collect));
        List<Long> attrIds = groupId.stream().map(item -> item.getAttrId()).collect(Collectors.toList());

        //2.3)、从当前分类的所有属性中移除这些属性；
        QueryWrapper<Attr> wrapper = new QueryWrapper<Attr>().eq("category_id", categoryId).eq("attr_type", ProductConst.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds != null && attrIds.size() > 0) {
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> w.eq("attr_id", key).or().like("attr_name", key));
        }
        IPage<Attr> page = this.page(new Query<Attr>().getPage(params), wrapper);

        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }

    @Override
    public void deleteRelation(List<AttrGroupRelationVo> vos) {

        QueryWrapper<AttrAttrgroupRelation> wrapper = new QueryWrapper<>();
        for (AttrGroupRelationVo vo : vos) {
            wrapper.or(w ->
                    w.eq("attr_id", vo.getAttrId()).eq("attr_group_id", vo.getAttrGroupId())
            );
        }

        attrAttrgroupRelationMapper.delete(wrapper);
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        Attr attrEntity = new Attr();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConst.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //1、修改分组关联
            AttrAttrgroupRelation relationEntity = new AttrAttrgroupRelation();

            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());

            Integer count = attrAttrgroupRelationMapper.selectCount(new QueryWrapper<AttrAttrgroupRelation>().eq("attr_id", attr.getAttrId()));
            if (count > 0) {

                attrAttrgroupRelationMapper.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelation>().eq("attr_id", attr.getAttrId()));

            } else {
                attrAttrgroupRelationMapper.insert(relationEntity);
            }
        }

    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        Attr attrEntity = new Attr();
//        attrEntity.setAttrName(attr.getAttrName());
        BeanUtils.copyProperties(attr, attrEntity);
        //1、保存基本数据
        System.out.println(attrEntity);
        this.save(attrEntity);
        //2、保存关联关系
        if (attr.getAttrType() == ProductConst.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelation relationEntity = new AttrAttrgroupRelation();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationMapper.insert(relationEntity);
        }
    }
}
