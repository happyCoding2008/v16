package com.qf.v16.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述购物车的每个条目
 * @author huangguizhao
 */
public class CartItem implements Serializable,Comparable<CartItem>{

    private Long productId;
    private Integer count;
    private Date updateTime;

    public CartItem(Long productId, Integer count, Date updateTime) {
        this.productId = productId;
        this.count = count;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", count=" + count +
                ", updateTime=" + updateTime +
                '}';
    }

    public CartItem(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int compareTo(CartItem o) {
        return (int) (o.getUpdateTime().getTime()-this.getUpdateTime().getTime());
    }
}
