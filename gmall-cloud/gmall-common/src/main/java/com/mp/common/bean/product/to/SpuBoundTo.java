package com.mp.common.bean.product.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Author: Xiaoer
 * Date: 2020-06-23
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
