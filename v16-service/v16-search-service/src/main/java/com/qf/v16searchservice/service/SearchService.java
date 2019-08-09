package com.qf.v16searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.ISearchService;
import com.qf.v16.common.pojo.ResultBean;
import com.qf.v16.entity.TProduct;
import com.qf.v16.mapper.TProductMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        //1.构建查询条件
        SolrQuery queryCondition = new SolrQuery();
        if(!StringUtils.isAnyEmpty(keywords)){
            queryCondition.setQuery("product_name:"+keywords);
        }else{
            queryCondition.setQuery("product_name:mate30");
        }
        //添加高亮的效果--step1
        queryCondition.setHighlight(true);
        queryCondition.addHighlightField("product_name");
        //queryCondition.addHighlightField("other");
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");

        //设置分页
        /*queryCondition.setStart((pageIndex-1)*pageSize);
        queryCondition.setRows(pageSize);*/


        //2.执行查询，得到结果集
        try {
            QueryResponse response = solrClient.query(queryCondition);
            SolrDocumentList results = response.getResults();
            //3.需要将这个结果集转换为List<TProduct>
            List<TProduct> target = new ArrayList<>(results.size());
            //获取高亮信息 map 2
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //String key 1
            //String key 2

            //String key product_name
            //String key sale_point

            for (SolrDocument document : results) {
                //document->product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setImages(document.getFieldValue("product_images").toString());

                //设置高亮信息
                Map<String, List<String>> idHighlight = highlighting.get(document.getFieldValue("id").toString());
                //
                List<String> productNameHighLight = idHighlight.get("product_name");
                //
                if(productNameHighLight != null){
                    product.setName(productNameHighLight.get(0));
                }else{
                    product.setName(document.getFieldValue("product_name").toString());
                }
                target.add(product);
            }
            //
            return new ResultBean("200",target);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("500","查询出现错误！");
        }
    }


    @Override
    public ResultBean updateById(Long id) {
        //1.获取到数据
        TProduct product = productMapper.selectByPrimaryKey(id);
        //2.将对象同步到索引库
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
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("500","提交到索引库失败！");
        }
        return new ResultBean("200","同步完成！");
    }


}
