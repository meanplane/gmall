package com.mp.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.ware.WareInfo;
import com.mp.common.utils.PageUtils;

import java.util.Map;

/**
 * 仓库信息
 */
public interface WareInfoService extends IService<WareInfo> {

    PageUtils queryPage(Map<String, Object> params);
}

