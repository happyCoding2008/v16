package com.qf.v16.common.pojo;

import java.io.Serializable;

/**
 * @author huangguizhao
 * 统一后端给前端返回的数据类型
 * {}
 */
public class ResultBean<T> implements Serializable {
    private String statusCode;
    private T data;

    public ResultBean(String statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public ResultBean() {
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
