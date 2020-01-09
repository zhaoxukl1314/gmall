package com.zhaoxu.manageservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhaoxu.bean.PmsBaseAttrInfo;
import com.zhaoxu.bean.PmsBaseAttrValue;
import com.zhaoxu.bean.PmsBaseSaleAttr;
import com.zhaoxu.manageservice.mapper.PmsAttrInfoMapper;
import com.zhaoxu.manageservice.mapper.PmsAttrValueMapper;
import com.zhaoxu.manageservice.mapper.PmsBaseSaleMapper;
import com.zhaoxu.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsAttrInfoMapper pmsAttrInfoMapper;

    @Autowired
    PmsAttrValueMapper pmsAttrValueMapper;

    @Autowired
    PmsBaseSaleMapper pmsBaseSaleMapper;

    @Override
    public List<PmsBaseAttrInfo> getPmsBaseAttrInfo(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsAttrInfoMapper.select(pmsBaseAttrInfo);
        for (PmsBaseAttrInfo baseAttrInfo: pmsBaseAttrInfos) {
            List<PmsBaseAttrValue> pmsBaseAttrValues = new ArrayList<>();
            PmsBaseAttrValue baseAttrValue = new PmsBaseAttrValue();
            baseAttrValue.setAttrId(baseAttrInfo.getId());
            pmsBaseAttrValues = pmsAttrValueMapper.select(baseAttrValue);
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String id = pmsBaseAttrInfo.getId();
        if (StringUtils.isBlank(id)) {
            pmsAttrInfoMapper.insertSelective(pmsBaseAttrInfo);

            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }

        } else {
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            pmsAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setId(pmsBaseAttrInfo.getId());
            pmsAttrValueMapper.delete(pmsBaseAttrValue);
            for (PmsBaseAttrValue value : attrValueList) {
                pmsAttrValueMapper.insertSelective(value);
            }
        }
        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> result = pmsAttrValueMapper.select(pmsBaseAttrValue);
        return result;
    }

    @Override
    public List<PmsBaseSaleAttr> getPmsBaseSaleAttr() {
        return pmsBaseSaleMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {

        String valueIdStr = StringUtils.join(valueIdSet, ",");//41,45,46
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsAttrInfoMapper.selectAttrValueListByValueId(valueIdStr);
        return pmsBaseAttrInfos;
    }
}
