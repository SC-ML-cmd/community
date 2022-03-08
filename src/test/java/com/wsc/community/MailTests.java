package com.wsc.community;

import com.wsc.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) //设置配置类
public class MailTests {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail(){

        mailClient.sendMail("wangshuaichuan1@163.com", "TEST", "Welcome.");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username", "wsc");
        String contextStr = templateEngine.process("/mail/demo", context);
        System.out.println(contextStr);

        mailClient.sendMail("wangshuaichuan1@163.com", "TEST", contextStr);
    }
}
