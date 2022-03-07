package com.wsc.community.Service;

import com.wsc.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Scope("prototype")
@Service
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;


    public AlphaService(){
        System.out.println("实例化AlphaService");
    }

    @PostConstruct //初始化方法
    public void init(){
        System.out.println("初始化AlphaService");
    }

    @PreDestroy //销毁方法
    public void destroy(){
        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }
}
