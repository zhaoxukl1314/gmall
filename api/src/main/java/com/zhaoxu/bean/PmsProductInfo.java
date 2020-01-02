package com.zhaoxu.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @param
 * @return
 */

public class PmsProductInfo implements Serializable {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    @JsonProperty("spuName")
    private String productName;

    @Column
    private String description;

    @Column
    private  String catalog3Id;

    @Transient
    @JsonProperty("spuSaleAttrList")
    private List<PmsProductSaleAttr> pmsProductSaleAttrList;
    @Transient
    @JsonProperty("spuImageList")
    private List<PmsProductImage> pmsProductImageList;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<PmsProductSaleAttr> getPmsProductSaleAttrList() {
        return pmsProductSaleAttrList;
    }

    public void setPmsProductSaleAttrList(List<PmsProductSaleAttr> pmsProductSaleAttrList) {
        this.pmsProductSaleAttrList = pmsProductSaleAttrList;
    }

    public List<PmsProductImage> getPmsProductImageList() {
        return pmsProductImageList;
    }

    public void setPmsProductImageList(List<PmsProductImage> pmsProductImageList) {
        this.pmsProductImageList = pmsProductImageList;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

}


