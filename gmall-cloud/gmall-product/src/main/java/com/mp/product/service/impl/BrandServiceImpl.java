package com.mp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.Brand;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.product.mapper.BrandMapper;
import com.mp.product.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-16
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<Brand> query = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            query.eq("brand_id", key).or().like("name", key);
        }

        IPage<Brand> page = this.page(new Query<Brand>().getPage(params), query);

        return new PageUtils(page);
    }
}
