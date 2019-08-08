package com.qf.v16searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.ISearchService;
import com.qf.v16.common.pojo.ResultBean;
import com.qf.v16.entity.TProduct;
import com.qf.v16.mapper.TProductMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * @author huangguizhao
 */
@Service
public class SearchService implements ISearchService {

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public ResultBean synAllData() {
        //1.从数据库获取到数据源
        //TODO 性能 提供一个新的接口，只查询需要的数据
        List<TProduct> list = productMapper.list();
        //2.将这批数据同步到索引库
        for (TProduct product : list) {
            //3.product -> document
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id",product.getId());
            document.setField("product_name",product.getName());
            document.setField("product_price",product.getPrice());
            document.setField("product_sale_point",product.getSalePoint());
            document.setField("product_images",product.getImages());
            //4.添加
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e) {
                //TODO 如果处理方式不同，则应该分别catch处理
                e.printStackTrace();
                return new ResultBean("500","添加索引库失败！");
            }
        }
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("500","提交到索引库失败！");
        }
        return new ResultBean("200","同步完成！");
    }

    @Override
    public ResultBean queryByKeywords(String keywords) {
        return null;
    }
}
