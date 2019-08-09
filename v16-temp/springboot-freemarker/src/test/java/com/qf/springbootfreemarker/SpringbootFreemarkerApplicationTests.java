package com.qf.springbootfreemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootFreemarkerApplicationTests {

	@Autowired
	private Configuration configuration;

	@Test
	public void createTest() throws IOException, TemplateException {
		//1.获取到模板对象
		Template template = configuration.getTemplate("freemarker.ftl");
		//2.构建数据
		Map<String,Object> data = new HashMap<>();
		//2.1 普通字符串
		data.put("username","java");
		//2.2 自定义对象
		Product product = new Product(1L,"花旗参红枣枸杞",999L,new Date());
		data.put("product",product);
		//2.3 存储集合
		List<Product> list = new ArrayList<>();
		list.add(new Product(1L,"花旗参红枣枸杞",999L,new Date()));
		list.add(new Product(2L,"霸王洗发水",999L,new Date()));
		data.put("list",list);
		//2.4 判断
		data.put("age",19);
		//2.5 空值
		data.put("nullObject",null);
		//3.模板+数据=输出
		FileWriter out = new FileWriter(
				"D:\\dev2\\springboot666\\v16\\v16-temp\\springboot-freemarker\\src\\main\\resources\\templates\\freemarker.html");
		template.process(data,out);
		System.out.println("success!!!!");
	}

}
