package com.zhaoxu.searchweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhaoxu.bean.PmsSearchParam;
import com.zhaoxu.bean.PmsSearchSkuInfo;
import com.zhaoxu.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class SearchController {

    @Reference
    SearchService searchService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfos);
        return "list.html";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }
}
