package com.qf.v16.api.vo;

import com.qf.v16.entity.TProduct;
import org.omg.IOP.TaggedProfile;

import java.io.Serializable;

/**
 * @author huangguizhao
 */
public class ProductVO implements Serializable{
    private TProduct product;
    private String productDesc;

    public ProductVO(TProduct product, String productDesc) {
        this.product = product;
        this.productDesc = productDesc;
    }

    public ProductVO() {
    }

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
}
