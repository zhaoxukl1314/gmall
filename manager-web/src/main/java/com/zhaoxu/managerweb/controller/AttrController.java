package com.zhaoxu.managerweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhaoxu.bean.PmsBaseAttrInfo;
import com.zhaoxu.bean.PmsBaseAttrValue;
import com.zhaoxu.bean.PmsBaseSaleAttr;
import com.zhaoxu.service.AttrService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class AttrController {

    @Reference
    AttrService attrService;

    @RequestMapping("/attrInfoList")
    public List<PmsBaseAttrInfo> attrInfos(String catalog3Id) {
        List<PmsBaseAttrInfo> baseAttrInfos = attrService.getPmsBaseAttrInfo(catalog3Id);
        return baseAttrInfos;
    }

    @RequestMapping("/saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {

        String result = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return result;
    }

    @RequestMapping("/getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {

        List<PmsBaseAttrValue> result = attrService.getAttrValueList(attrId);
        return result;
    }

    @RequestMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        List<PmsBaseSaleAttr> pmsBaseSaleAttr = attrService.getPmsBaseSaleAttr();
        return pmsBaseSaleAttr;
    }
}
