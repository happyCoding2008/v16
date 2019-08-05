package com.qf.v16.mapper;

import com.qf.v16.common.base.IBaseDao;
import com.qf.v16.entity.TProduct;

import java.util.List;

public interface TProductMapper extends IBaseDao<TProduct> {

    List<TProduct> list();
}