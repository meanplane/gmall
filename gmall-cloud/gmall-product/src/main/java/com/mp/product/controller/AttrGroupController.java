package com.mp.product.controller;

import com.mp.common.bean.product.Attr;
import com.mp.common.bean.product.AttrGroup;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.R;
import com.mp.product.service.AttrAttrGroupRelationService;
import com.mp.product.service.AttrGroupService;
import com.mp.product.service.AttrService;
import com.mp.product.service.CategoryService;
import com.mp.product.vo.AttrGroupRelationVo;
import com.mp.product.vo.AttrGroupWithAttrsVo;
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

    @Autowired
    private AttrAttrGroupRelationService attrAttrGroupRelationService;

    ///product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){

        attrAttrGroupRelationService.saveBatch(vos);
        return R.ok();
    }

    //product/attrgroup/{categoryId}/withattr
    @GetMapping("/{categoryId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("categoryId")Long catelogId){

        //1、查出当前分类下的所有属性分组，
        //2、查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> vos =  attrGroupService.getAttrGroupWithAttrsByCategoryId(catelogId);
        return R.ok().put("data",vos);
    }

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

    // 删除关联关系
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody  List<AttrGroupRelationVo> vos){
        attrService.deleteRelation(vos);
        return R.ok();
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
