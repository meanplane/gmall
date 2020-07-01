package com.mp.test;

import com.alibaba.fastjson.JSON;
import com.mp.common.bean.product.Brand;
import com.mp.search.SearchApp;
import com.mp.search.config.ESconfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Author: Xiaoer
 * Date: 2020-07-01
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = SearchApp.class)
public class Test1 {
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void test1() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");

        Brand brand = new Brand();
        brand.setName("华为");
        String str = JSON.toJSONString(brand);


        indexRequest.source(str, XContentType.JSON);
        IndexResponse index = client.index(indexRequest, ESconfig.COMMON_POTIONS);
        System.out.println(index);
    }

    @Test
    public void testSearch() throws IOException {
        // 1.创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("bank");
        // 指定DSL,检索条件
        // SearchSourceBuilder sourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 1.1 构造检索条件
//        sourceBuilder.query();
//        sourceBuilder.from();
//        sourceBuilder.size();
//        sourceBuilder.aggregation();
        sourceBuilder.query(QueryBuilders.matchQuery("address","mill"));

        // 1.2 按照年龄分布
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);

        // 1.3 平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        searchRequest.source(sourceBuilder);
        // 2.执行检索
        SearchResponse res = client.search(searchRequest, ESconfig.COMMON_POTIONS);

        // 3.分析结果
        System.out.println(res);

        Aggregations aggs = res.getAggregations();
        Aggregation agg = aggs.get("ageAgg");
//        agg.ge
        System.out.println(ageAgg);
    }



}
