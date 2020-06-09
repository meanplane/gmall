package com.mp.common.bean.coupon;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-09
 */
@Data
@TableName("sms_coupon_spu_relation")
public class CouponSpuRelation {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * spu_id
     */
    private Long spuId;
    /**
     * spu_name
     */
    private String spuName;

}
