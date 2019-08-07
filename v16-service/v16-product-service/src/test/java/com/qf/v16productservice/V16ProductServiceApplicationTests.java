package com.qf.v16productservice;

import com.github.pagehelper.PageInfo;
import com.qf.v16.api.IProductService;
import com.qf.v16.entity.TProduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V16ProductServiceApplicationTests {

	@Autowired
	private IProductService productService;

	@Autowired
	private DataSource dataSource;

	@Test
	public void contextLoads() {
		TProduct product = productService.selectByPrimaryKey(1L);
		System.out.println(product.getName()+"->"+product.getPrice());
	}

	@Test
	public void testConnection() throws SQLException {
		System.out.println(dataSource.getConnection());
	}

	@Test
	public void testPage(){
		PageInfo<TProduct> pageInfo = productService.page(1, 1);
		List<TProduct> list = pageInfo.getList();
		for (TProduct tProduct : list) {
			System.out.println(tProduct.getName()+"->"+tProduct.getPrice());
		}
	}

}
