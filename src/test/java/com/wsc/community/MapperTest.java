package com.wsc.community;

import com.wsc.community.Service.UserService;
import com.wsc.community.dao.DiscussPostMapper;
import com.wsc.community.dao.UserMapper;
import com.wsc.community.entity.DiscussPost;
import com.wsc.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) //设置配置类
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserService userService;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(155);
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
        //修改代码IDEA提交测试
        List<DiscussPost> res = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for(DiscussPost discussPost : res){
            System.out.println(discussPost);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
    @Test
    public void test(){
        Solution solution = new Solution();
//        System.out.println(solution.countSubstrings("aaa"));
    }

    class Solution {
        public int[] dailyTemperatures(int[] temperatures) {
            int[] result = new int[temperatures.length];
            Deque<Integer> stack = new LinkedList<>();

            for(int i = 0; i < temperatures.length; i++){
                //大于则循环弹出
                while(!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]){
                    int index = stack.pop();
                    result[index] = i - index;
                }

                //小于则压栈
                stack.push(i);
            }

            return result;
        }
    }

    @Test
    public void test2(){
        userService.updatePassword(155, "1234", "1234");
    }
}
