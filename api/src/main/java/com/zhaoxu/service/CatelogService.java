package com.zhaoxu.service;

import com.zhaoxu.bean.PmsBaseCatalog1;
import com.zhaoxu.bean.PmsBaseCatalog2;
import com.zhaoxu.bean.PmsBaseCatalog3;

import java.util.List;

public interface CatelogService {
    List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog2(String catelog1Id);

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
