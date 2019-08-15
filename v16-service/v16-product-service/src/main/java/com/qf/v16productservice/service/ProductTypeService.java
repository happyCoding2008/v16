package com.qf.v16productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v16.api.IProductTypeService;
import com.qf.v16.common.base.BaseServiceImpl;
import com.qf.v16.common.base.IBaseDao;
import com.qf.v16.entity.TProductType;
import com.qf.v16.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author huangguizhao
 */
@Service
public class ProductTypeService extends BaseServiceImpl<TProductType> implements IProductTypeService {

    @Autowired
    private TProductTypeMapper productTypeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return productTypeMapper;
    }

    @Override
    public List<TProductType> list() {
        //1.去缓存获取该集合信息
        String key = "productType:list";
        List<TProductType> cacheList = (List<TProductType>) redisTemplate.opsForValue().get(key);
        //2.判断缓存中是否存在
        if(cacheList != null){
            //直接从缓存返回数据
            return cacheList;
        }
        //3.缓存不存在则查询数据库
        System.out.println("从数据库中获取数据.....");
        List<TProductType> list = super.list();
        //4.判断
        if(list != null){
            //将数据保存到缓存中
            redisTemplate.opsForValue().set(key,list);
        }
        return list;
    }
}
