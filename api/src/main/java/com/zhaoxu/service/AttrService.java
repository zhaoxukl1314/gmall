package com.zhaoxu.service;

import com.zhaoxu.bean.PmsBaseAttrInfo;
import com.zhaoxu.bean.PmsBaseAttrValue;
import com.zhaoxu.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrService {
    List<PmsBaseAttrInfo> getPmsBaseAttrInfo(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> getPmsBaseSaleAttr();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);

}
