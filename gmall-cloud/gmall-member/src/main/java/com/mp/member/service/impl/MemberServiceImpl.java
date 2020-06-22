package com.mp.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.member.Member;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.member.mapper.MemberMapper;
import com.mp.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-22
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Member> page = this.page(
                new Query<Member>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

}
