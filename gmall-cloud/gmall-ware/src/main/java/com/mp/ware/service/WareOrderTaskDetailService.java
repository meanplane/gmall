package com.mp.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.ware.WareOrderTaskDetail;
import com.mp.common.utils.PageUtils;

import java.util.Map;

/**
 * 库存工作单
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetail> {

    PageUtils queryPage(Map<String, Object> params);
}

