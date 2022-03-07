package com.wsc.community;

import com.wsc.community.dao.DiscussPostMapper;
import com.wsc.community.dao.UserMapper;
import com.wsc.community.entity.DiscussPost;
import com.wsc.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) //设置配置类
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }
    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("zhangsan");
        user.setPassword("12345");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("https://www.nowcoder.com/101.png");
        int i = userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser(){
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);
        System.out.println(userMapper.selectById(150).getUsername());

        rows = userMapper.updateHeader(150, "https://www.nowcoder.com/102.png");
        System.out.println(rows);
        System.out.println(userMapper.selectById(150).getHeaderUrl());

        rows = userMapper.updatePassword(150, "wsc");
        System.out.println(rows);
    }

    @Test
    public void testSelectPosts(){
        List<DiscussPost> res = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for(DiscussPost discussPost : res){
            System.out.println(discussPost);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
}
