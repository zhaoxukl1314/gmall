package com.zhaoxu.managerweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhaoxu.bean.PmsProductInfo;
import com.zhaoxu.bean.PmsProductSaleAttr;
import com.zhaoxu.service.SpuService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping("spuList")
    public List<PmsProductInfo> spuList(String catalog3Id) {
        List<PmsProductInfo> result = spuService.spuList(catalog3Id);
        return result;
    }

    @RequestMapping("saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {

        return "success";
    }

    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file")MultipartFile multipartFile) {

        return "success";
    }

    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        List<PmsProductSaleAttr> list = spuService.spuSaleAttrList(spuId);
        return list;
    }
}
