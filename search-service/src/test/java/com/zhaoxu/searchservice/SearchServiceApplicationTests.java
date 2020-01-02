package com.zhaoxu.searchservice;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhaoxu.bean.PmsSkuInfo;
import com.zhaoxu.service.SkuService;
import io.searchbox.client.JestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SearchServiceApplicationTests {

    @Reference
    SkuService skuService;

    @Autowired
    JestClient jestClient;

    @Test
    void contextLoads() throws IOException {

        List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();
    }

}
