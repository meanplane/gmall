package com.mp.common.bean.coupon;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Author: Xiaoer
 * Date: 2020-06-09
 */
@Data
@TableName("sms_coupon_spu_category_relation")
public class CouponSpuCategoryRelation implements Serializable {
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
     * 产品分类id
     */
    private Long categoryId;
    /**
     * 产品分类名称
     */
    private String categoryName;
}
