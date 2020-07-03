package com.mp.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.product.SpuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * spu信息
 */
@Mapper
public interface SpuInfoMapper extends BaseMapper<SpuInfo> {

    @Update("update `pms_spu_info` set publish_status=#{code}, update_time=NOW() where id=#{spuId} ")
    int updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
