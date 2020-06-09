package com.mp.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.product.AttrAttrgroupRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
@Mapper
public interface AttrAttrgroupRelationMapper extends BaseMapper<AttrAttrgroupRelation> {

    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelation> entities);

}
