package com.shiju.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.Map;

/**
 * @author shiju
 * @date 2021/06/21 10:24
 */
public class Goods {
    private Integer id;  //编号
    private String title; //标题
    private Double price; //价格
    private Integer stock; //库存
    private Integer saleNum;  //销售量
    private Date createTime; //创建时间
    private String categoryName; //分类名称
    private String brandName; //品牌名称
    private Map<String, Object> spec; //规格

    @JSONField(serialize = false) //该字段不会序列化成json
    private String specStr; //规格json数据

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Map<String, Object> getSpec() {
        return spec;
    }

    public void setSpec(Map<String, Object> spec) {
        this.spec = spec;
    }

    public String getSpecStr() {
        return specStr;
    }

    //getter、setter、toString方法自己添加，setSpecStr的方法稍作改进
    public void setSpecStr(String specStr) {
        this.specStr = specStr;
        //使用fastjson将json数据转换成map集合,封装给spec
        this.spec = JSON.parseObject(specStr, Map.class);
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", saleNum=" + saleNum +
                ", createTime=" + createTime +
                ", categoryName='" + categoryName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", spec=" + spec +
                ", specStr='" + specStr + '\'' +
                '}';
    }
}
