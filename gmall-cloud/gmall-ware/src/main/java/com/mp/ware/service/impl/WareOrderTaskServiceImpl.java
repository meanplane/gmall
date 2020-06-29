package com.mp.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.ware.WareOrderTask;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.ware.mapper.WareOrderTaskMapper;
import com.mp.ware.service.WareOrderTaskService;
import org.springframework.stereotype.Service;

import java.util.Map;



@Service("wareOrderTaskService")
public class WareOrderTaskServiceImpl extends ServiceImpl<WareOrderTaskMapper, WareOrderTask> implements WareOrderTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareOrderTask> page = this.page(
                new Query<WareOrderTask>().getPage(params),
                new QueryWrapper<WareOrderTask>()
        );

        return new PageUtils(page);
    }

}
