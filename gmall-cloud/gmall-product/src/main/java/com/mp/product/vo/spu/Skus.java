package com.mp.product.vo.spu;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: Xiaoer
 * Date: 2020-06-23
 */
@Data
public class Skus {
    private List<Attr> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<Images> images;
    private List<String> descar;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
