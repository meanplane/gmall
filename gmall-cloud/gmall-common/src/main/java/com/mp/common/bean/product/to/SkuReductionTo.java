package com.mp.common.bean.product.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: Xiaoer
 * Date: 2020-06-23
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
