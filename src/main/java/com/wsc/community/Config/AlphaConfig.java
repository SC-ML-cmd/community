package com.wsc.community.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration  //设置配置类
public class AlphaConfig {
    @Bean //方法名就是Bean名字 这个方法返回的对象将被装备到容器中
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
