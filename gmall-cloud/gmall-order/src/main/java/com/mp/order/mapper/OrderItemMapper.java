package com.mp.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.common.bean.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-08 09:56:16
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
	
}
