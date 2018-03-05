package cn.epaylinks.boot;

import java.io.IOException;
import java.util.Hashtable;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import cn.epaylinks.controller.UserController;

/**
 * Hello world!
 *
 */
@Controller
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("cn.epaylinks")
@MapperScan("cn.epaylinks.mapper")
public class App
{
	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource()
	{
		return new org.apache.tomcat.jdbc.pool.DataSource();
	}

	@Bean
	public SqlSessionFactory SqlSessionFactoryBean() throws Exception
	{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}
	
	public PlatformTransactionManager transactionManager()
	{
		return new DataSourceTransactionManager(dataSource());
	}

	@RequestMapping("/")
	String index(HttpServletRequest request,HttpServletResponse response)
	{
		System.out.println("进入首页,先生成UUID");
		
		request.setAttribute("uuid", UUID.randomUUID());
		
		return "pages/index";
	}
	
	@RequestMapping("/main")
	String main()
	{
		System.out.println("进入主页面");
		return "pages/main";
	}

	public static void main(String[] args)
	{
		SpringApplication.run(new Object[] { App.class, UserController.class },
				args);
	}
}
