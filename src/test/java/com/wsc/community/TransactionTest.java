package com.wsc.community;

import com.wsc.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) //设置配置类
public class TransactionTest {

    @Autowired
    AlphaService alphaService;

    @Test
    public void test(){
        Object object = alphaService.save1();
        System.out.println(object);
    }

    @Test
    public void testSave2(){
        Object object = alphaService.save2();
        System.out.println(object);
    }
}
