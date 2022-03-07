package com.wsc.community;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) //设置配置类
public class LoggerTests {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTests.class);

    @Test
    public void test(){
        System.out.println(logger);

        logger.debug("debug log"); //调试程序
        logger.info("info log");   //可以错误的地方特殊任务
        logger.warn("warn log");
        logger.error("error log"); //捕捉错误后
        //Spring boot 整合了logback的配置文件
    }
}
