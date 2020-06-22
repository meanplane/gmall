package com.mp.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.common.bean.member.MemberLevel;
import com.mp.common.utils.PageUtils;

import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-22
 */
public interface MemberLevelService extends IService<MemberLevel> {
    PageUtils queryPage(Map<String, Object> params);
}
