package com.mp.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.member.MemberLevel;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.member.mapper.MemberLevelMapper;
import com.mp.member.service.MemberLevelService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-22
 */
@Service
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel> implements MemberLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberLevel> page = this.page(
                new Query<MemberLevel>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

}
