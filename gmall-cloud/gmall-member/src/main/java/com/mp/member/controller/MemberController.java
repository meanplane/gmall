package com.mp.member.controller;

import com.mp.common.bean.member.Member;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.R;
import com.mp.member.feign.CouponFeignService;
import com.mp.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-10
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/coupons")
    public R test() {
        Member member = new Member();
        member.setNickname("张三");

        R membercoupons = couponFeignService.membercoupons();
        return R.ok().put("member", member).put("coupons", membercoupons.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id) {
        Member member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody Member member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody Member member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody List<Long> ids) {
        memberService.removeByIds(ids);
        return R.ok();
    }
}
