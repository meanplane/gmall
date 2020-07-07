package com.mp.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: Xiaoer
 * Date: 2020-07-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category2VO {

    private String categoryId;

    private List<Category3VO> category3List;

    private String id;

    private String name;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category3VO {

        private String category2Id;

        private String id;

        private String name;

    }

}

