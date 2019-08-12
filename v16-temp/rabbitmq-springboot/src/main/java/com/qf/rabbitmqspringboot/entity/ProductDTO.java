package com.qf.rabbitmqspringboot.entity;

import java.io.Serializable;

/**
 * @author huangguizhao
 */
public class ProductDTO implements Serializable{

    private Long id;
    private String name;

    public ProductDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductDTO() {
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
}
