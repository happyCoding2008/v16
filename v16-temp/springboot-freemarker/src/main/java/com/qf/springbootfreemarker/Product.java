package com.qf.springbootfreemarker;

import java.util.Date;

/**
 * @author huangguizhao
 */
public class Product {

    private Long id;
    private String name;
    private Long price;
    private Date createTime;

    public Product(Long id, String name, Long price,Date createTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createTime = createTime;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
