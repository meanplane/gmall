package com.mp.product.controller;

import com.mp.common.bean.product.Attr;
import com.mp.common.bean.product.AttrGroup;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.R;
import com.mp.product.service.AttrGroupService;
import com.mp.product.service.AttrService;
import com.mp.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-18
 */
@RestController
@RequestMapping("/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    //关联 product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<Attr> entities =  attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    //不关联 product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }

    @RequestMapping("/list/{cid}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("cid") Long cid) {

        PageUtils page = attrGroupService.queryPage(params, cid);
        return R.ok().put("page", page);
    }

    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
        AttrGroup attrGroup = attrGroupService.getById(attrGroupId);

        Long categoryId = attrGroup.getCategoryId();
        // 根据 分类id 查找父类id
        List<Long> path = categoryService.findCategoryPath(categoryId);

        attrGroup.setCategoryPath(path);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroup attrGroup){
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroup attrGroup){
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody List<Long> attrGroupIds){
        attrGroupService.removeByIds(attrGroupIds);

        return R.ok();
    }
}
