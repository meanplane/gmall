package com.mp.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.ware.WareOrderTaskDetail;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.ware.mapper.WareOrderTaskDetailMapper;
import com.mp.ware.service.WareOrderTaskDetailService;
import org.springframework.stereotype.Service;

import java.util.Map;



@Service("wareOrderTaskDetailService")
public class WareOrderTaskDetailServiceImpl extends ServiceImpl<WareOrderTaskDetailMapper, WareOrderTaskDetail> implements WareOrderTaskDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareOrderTaskDetail> page = this.page(
                new Query<WareOrderTaskDetail>().getPage(params),
                new QueryWrapper<WareOrderTaskDetail>()
        );

        return new PageUtils(page);
    }

}
