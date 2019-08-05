package com.qf.v16.api;

import com.github.pagehelper.PageInfo;
import com.qf.v16.common.base.IBaseService;
import com.qf.v16.entity.TProduct;

import java.util.List;

/**
 * @author huangguizhao
 */
public interface IProductService extends IBaseService<TProduct>{
    //增加一些非基本增删改查的方法
    public List<TProduct> list();

    public PageInfo<TProduct> page(Integer pageIndex,Integer pageSize);
}
