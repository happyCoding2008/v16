package com.qf.v16productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.v16.api.IProductService;
import com.qf.v16.api.vo.ProductVO;
import com.qf.v16.common.base.BaseServiceImpl;
import com.qf.v16.common.base.IBaseDao;
import com.qf.v16.entity.TProduct;
import com.qf.v16.entity.TProductDesc;
import com.qf.v16.mapper.TProductDescMapper;
import com.qf.v16.mapper.TProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author huangguizhao
 * 发布服务（引入Dubbo相关的依赖及配置）
 */
@Service
public class ProductService extends BaseServiceImpl<TProduct> implements IProductService{

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private TProductDescMapper productDescMapper;

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

    @Override
    @Transactional
    public Long add(ProductVO vo) {
        //1.添加商品的基本信息（主键回填）
        TProduct product = vo.getProduct();
        productMapper.insertSelective(product);
        //2.添加商品的描述信息
        TProductDesc desc = new TProductDesc();
        desc.setProductId(product.getId());
        desc.setProductDesc(vo.getProductDesc());
        productDescMapper.insertSelective(desc);
        return product.getId();
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        //将物理删除修改为逻辑删除
        TProduct product = new TProduct();
        product.setId(id);
        product.setFlag(false);
        product.setUpdateTime(new Date());
        return productMapper.updateByPrimaryKeySelective(product);
        //update set flag=0 where id=?
    }
}
