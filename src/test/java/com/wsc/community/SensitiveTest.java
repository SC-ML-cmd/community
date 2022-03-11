package com.wsc.community;

import com.wsc.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) //设置配置类
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitive(){
        String text = "这里可以☆赌****博，可以嫖*娼，可以吸毒，可以开票";
        System.out.println(sensitiveFilter.filter(text));
    }
}
