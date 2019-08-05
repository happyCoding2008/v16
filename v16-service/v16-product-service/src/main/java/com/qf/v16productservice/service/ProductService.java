package com.qf.v16productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.v16.api.IProductService;
import com.qf.v16.common.base.BaseServiceImpl;
import com.qf.v16.common.base.IBaseDao;
import com.qf.v16.entity.TProduct;
import com.qf.v16.mapper.TProductMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author huangguizhao
 * 发布服务（引入Dubbo相关的依赖及配置）
 */
@Service
public class ProductService extends BaseServiceImpl<TProduct> implements IProductService{

    @Autowired
    private TProductMapper productMapper;

    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }

    @Override
    public List<TProduct> list() {
        return productMapper.list();
    }


    @Override
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize) {
        //1.設置分頁參數
        PageHelper.startPage(pageIndex,pageSize);
        //2.獲取到分頁數據
        List<TProduct> list = list();//2 1
        PageInfo<TProduct> pageInfo = new PageInfo<TProduct>(list,3);
        return pageInfo;
    }
}
