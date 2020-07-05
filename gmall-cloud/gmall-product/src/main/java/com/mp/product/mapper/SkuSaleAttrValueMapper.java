package com.mp.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.product.SkuSaleAttrValue;
import com.mp.product.vo.SkuItemSaleAttrVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
@Mapper
public interface SkuSaleAttrValueMapper extends BaseMapper<SkuSaleAttrValue> {
    @Select("select ssav.attr_id attr_id, ssav.attr_name attr_name,ssav.attr_value, group_concat(distinct info.sku_id) sku_ids " +
            "        from catmall_pms.pms_sku_info info " +
            "                 left join catmall_pms.pms_sku_sale_attr_value ssav on ssav.sku_id = info.sku_id " +
            "        where info.spu_id = #{spuId} " +
            "        group by ssav.attr_id, ssav.attr_name, ssav.attr_value")
    List<SkuItemSaleAttrVO> getSaleAttrsBySpuId(@Param("spuId") Long spuId);

    @Select("select concat(attr_name, ':', attr_value) from catmall_pms.pms_sku_sale_attr_value where sku_id=#{skuId}")
    List<String> getSkuSaleAttrValuesAsStringList(@Param("skuId") Long skuId);
}
