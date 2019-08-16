package com.qf.v16.api;

import com.qf.v16.common.pojo.ResultBean;

/**
 * @author huangguizhao
 */
public interface ICartService {

    /**
     * 添加商品到购物车
     * @param key 获取购物车的标识
     * @param productId 商品ID
     * @param count 购买的数量
     * @return
     */
    public ResultBean add(String key,Long productId,Integer count);

    /**
     * 删除商品
     * @param key 获取购物车的标识
     * @param productId 要删除的商品ID
     * @return
     */
    public ResultBean del(String key,Long productId);

    /**
     * 更新购物车的商品购买数量
     * @param key 获取购物车的标识
     * @param productId 商品ID
     * @param count 更新后的数量
     * @return
     */
    public ResultBean update(String key,Long productId,Integer count);

    /**
     * 查询我的购物车
     * @param key 获取购物车的标识
     * @return
     */
    public ResultBean query(String key);

    /**
     * 合并购物车
     * @param noLoginKey 未登录购物车的标识
     * @param loginKey 已登录购物车的标识
     * @return
     */
    public ResultBean merge(String noLoginKey,String loginKey);
}
