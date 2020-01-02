package com.zhaoxu.manageservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhaoxu.bean.PmsBaseCatalog1;
import com.zhaoxu.bean.PmsBaseCatalog2;
import com.zhaoxu.bean.PmsBaseCatalog3;
import com.zhaoxu.manageservice.mapper.PmsBaseCatalog1Mapper;
import com.zhaoxu.manageservice.mapper.PmsBaseCatalog2Mapper;
import com.zhaoxu.manageservice.mapper.PmsBaseCatalog3Mapper;
import com.zhaoxu.service.CatelogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatelogService {
    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catelog1Id) {
        PmsBaseCatalog2 catalog2 = new PmsBaseCatalog2();
        catalog2.setCatalog1Id(catelog1Id);
        return pmsBaseCatalog2Mapper.select(catalog2);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        PmsBaseCatalog3 catalog3 = new PmsBaseCatalog3();
        catalog3.setCatelog2Id(catalog2Id);
        return pmsBaseCatalog3Mapper.select(catalog3);
    }
}
