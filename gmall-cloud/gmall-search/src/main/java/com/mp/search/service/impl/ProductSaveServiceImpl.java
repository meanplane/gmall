package com.mp.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.mp.common.bean.to.to.SkuEsModel;
import com.mp.search.config.ESconfig;
import com.mp.search.constant.Esconstant;
import com.mp.search.service.ProductSaveService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Author: Xiaoer
 * Date: 2020-07-01
 */
@Service
public class ProductSaveServiceImpl implements ProductSaveService {
    @Autowired
    private RestHighLevelClient restClient;

    @Override
    public void productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 保存数据到 es中
        // 1.给es建立索引, product ,建立映射关系

        // 2.给es保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model : skuEsModels) {
            //1.构造保存请求
            IndexRequest indexRequest = new IndexRequest(Esconstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            indexRequest.source(s, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = restClient.bulk(bulkRequest, ESconfig.COMMON_POTIONS);


        // todo 处理错误
//        boolean b = bulk.hasFailures();
    }
}
