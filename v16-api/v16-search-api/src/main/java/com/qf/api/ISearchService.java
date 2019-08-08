package com.qf.api;

import com.qf.v16.common.pojo.ResultBean;

/**
 * @author huangguizhao
 */
public interface ISearchService {

    /**
     * 全量的数据同步
     * 将数据库的数据导入到索引库中（系统初始化时，执行一次）
     * @return
     */
    public ResultBean synAllData();

    public ResultBean queryByKeywords(String keywords);
}
