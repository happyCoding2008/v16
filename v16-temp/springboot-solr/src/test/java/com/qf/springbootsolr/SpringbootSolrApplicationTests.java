package com.qf.springbootsolr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootSolrApplicationTests {

	@Autowired
	private SolrClient solrClient;

	//数据库

	@Test
	public void addOrUpdateTest() throws IOException, SolrServerException {
		//1.document
		SolrInputDocument document = new SolrInputDocument();
		//2.设置字段的值
		document.setField("id","999");
		document.setField("product_name","mate90");
		document.setField("product_price","888888");
		document.setField("product_sale_point","能拍银河系");
		document.setField("product_images","999");
		//2.
		solrClient.add("collection2",document);
		solrClient.commit("collection2");
	}

	@Test
	public void queryTest() throws IOException, SolrServerException {
		//1.创建搜索条件
		SolrQuery query = new SolrQuery();
		//mate30 会进行分词，不是精确查找
		query.setQuery("product_name:mate30");
		//2.执行搜索
		QueryResponse response = solrClient.query(query);
		//
		SolrDocumentList results = response.getResults();
		for (SolrDocument result : results) {
			System.out.println("product_name:"+result.getFieldValue("product_name"));
			System.out.println("product_price:"+result.getFieldValue("product_price"));
		}
	}

	@Test
	public void delTest() throws IOException, SolrServerException {
		solrClient.deleteByQuery("product_name:mate30");
		solrClient.commit();
	}

}
