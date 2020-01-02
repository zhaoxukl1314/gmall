package com.zhaoxu.service;


import com.zhaoxu.bean.PmsSearchParam;
import com.zhaoxu.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
