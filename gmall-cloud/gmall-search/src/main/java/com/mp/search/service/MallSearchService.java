package com.mp.search.service;

import com.mp.search.vo.SearchParam;
import com.mp.search.vo.SearchResult;

/**
 * Author: Xiaoer
 * Date: 2020-07-06
 */
public interface MallSearchService {
    /**
     * 检索服务
     * @param searchParam 所有检索参数
     * @return 检索结果
     */
    SearchResult search(SearchParam searchParam);
}
