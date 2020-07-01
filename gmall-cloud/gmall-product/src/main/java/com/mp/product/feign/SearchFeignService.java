package com.mp.product.feign;

import com.mp.common.bean.to.to.SkuEsModel;
import com.mp.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Author: Xiaoer
 * Date: 2020-07-01
 */
@FeignClient("gmall-search")
public interface SearchFeignService {

    @PostMapping("/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
