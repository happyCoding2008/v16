package com.qf.v16.common.pojo;

import java.io.Serializable;

/**
 * @author huangguizhao
 * 根据我们引入的富文本插件，要求的返回的数据格式而设计的结果对象
 */
public class WangEditorResultBean implements Serializable {

    private String errno;
    private String[] data;

    public WangEditorResultBean(String errno, String[] data) {
        this.errno = errno;
        this.data = data;
    }

    public WangEditorResultBean() {
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
