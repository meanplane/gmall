package com.mp.search.service;

import com.mp.common.bean.to.to.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * Author: Xiaoer
 * Date: 2020-07-01
 */
public interface ProductSaveService {

    void productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
