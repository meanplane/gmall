package com.mp.common.bean.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 品牌分类关联
 */
@Data
@TableName("pms_category_brand_relation")
public class CategoryBrandRelation implements Serializable {

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 品牌id
	 */
	private Long brandId;
	/**
	 * 分类id
	 */
	private Long categoryId;
	/**
	 *
	 */
	private String brandName;
	/**
	 *
	 */
	private String categoryName;

}
