package com.qf.v16.api;

import com.github.pagehelper.PageInfo;
import com.qf.v16.api.vo.ProductVO;
import com.qf.v16.common.base.IBaseService;
import com.qf.v16.entity.TProduct;

import java.util.List;

/**
 * @author huangguizhao
 */
public interface IProductService extends IBaseService<TProduct>{

    public PageInfo<TProduct> page(Integer pageIndex,Integer pageSize);

    Long add(ProductVO vo);
}
