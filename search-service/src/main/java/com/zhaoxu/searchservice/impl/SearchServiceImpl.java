package com.zhaoxu.searchservice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhaoxu.bean.PmsSearchParam;
import com.zhaoxu.bean.PmsSearchSkuInfo;
import com.zhaoxu.service.SearchService;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {
        return null;
    }
}
