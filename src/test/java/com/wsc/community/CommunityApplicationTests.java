package com.wsc.community;

import com.wsc.community.Service.AlphaService;
import com.wsc.community.dao.AlphaDao;
import com.wsc.community.dao.AlphaDaoHibernateImpl;
import com.wsc.community.dao.LoginTicketMapper;
import com.wsc.community.entity.LoginTicket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) //设置配置类
class  CommunityApplicationTests implements ApplicationContextAware {

	//通过Spring容器向属性中注入对象
	@Autowired
	@Qualifier("alphaHibernate")
	private AlphaDao alphaDao;

	@Autowired
	private AlphaService alphaService;

	@Autowired
	private  SimpleDateFormat simpleDateFormat;

	@Autowired
	private LoginTicketMapper loginTicketMapper;

	@Test
	void contextLoads() {
		//Ioc核心是Spring容器
	}
	private ApplicationContext applicationContext;
	//ApplicationContext 就是Spring框架容器
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);
		// 类名 + hashCode
		// org.springframework.web.context.support.GenericWebApplicationContext@619bfe29, started on Fri Mar 04 18:10:12 CST 2022
		Object obj = new Object();
		System.out.println(obj);

		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());

		AlphaDao alphaDao1 = applicationContext.getBean("alphaHibernate", AlphaDao.class);
		System.out.println(alphaDao1.select());
	}

	@Test
	public void testBeanManagement(){
		//在容器中是单例的
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
		//可以设置为每次访问都返回新的实例 prototype
		AlphaService alphaService2 = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService2);
	}

	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}


	@Test
	public void testDI(){
		System.out.println(alphaDao);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}

	@Test
	public void testInsertLoginTicket(){
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setId(101);
		loginTicket.setTicket("abc");
		loginTicket.setStatus(0);
		loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
		int i = loginTicketMapper.insertLoginTicket(loginTicket);
		System.out.println(i);
	}

	@Test
	public void testSelectLoginTicket(){
		LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
		System.out.println(loginTicket.getStatus());

		loginTicketMapper.updateStatus(loginTicket.getTicket(), 1);
		System.out.println(loginTicketMapper.selectByTicket("abc").getStatus());
	}

}
