package com.mp.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.product.SpuComment;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.product.mapper.SpuCommentMapper;
import com.mp.product.service.SpuCommentService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class SpuCommentServiceImpl extends ServiceImpl<SpuCommentMapper, SpuComment> implements SpuCommentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuComment> page = this.page(
                new Query<SpuComment>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

}
