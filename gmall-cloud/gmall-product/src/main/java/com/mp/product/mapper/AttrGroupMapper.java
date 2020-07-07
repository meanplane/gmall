package com.mp.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.product.AttrGroup;
import com.mp.product.vo.SpuItemAttrGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 属性分组
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
@Mapper
public interface AttrGroupMapper extends BaseMapper<AttrGroup> {

    @Select("select pav.spu_id, ag.attr_group_name, ag.attr_group_id, aar.attr_id, attr.attr_name, pav.attr_value " +
            "        from gmall_pms.pms_attr_group ag " +
            "                 left join gmall_pms.pms_attr_attrgroup_relation aar on aar.attr_group_id = ag.attr_group_id " +
            "                 left join gmall_pms.pms_attr attr on attr.attr_id = aar.attr_id " +
            "                 left join gmall_pms.pms_product_attr_value pav on pav.attr_id = attr.attr_id " +
            "        where ag.category_id = #{categoryId} " +
            "          and pav.spu_id = #{spuId}")
    List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuIdAndCategoryId(@Param("spuId") Long spuId, @Param("categoryId") Long categoryId);
}
