package com.mp.product.vo.spu;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Author: Xiaoer
 * Date: 2020-06-23
 */
@Data
public class MemberPrice {
    private Long id;
    private String name;
    private BigDecimal price;
}
