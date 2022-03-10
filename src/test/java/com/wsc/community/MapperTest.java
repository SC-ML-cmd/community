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

import java.util.Arrays;
import java.util.List;

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
        System.out.println(solution.countSubstrings("aaa"));
    }

    class Solution {
        //一维解决不了，就想二维
        //回文子串，若两边元素相等，中间的元素为回文串，则整体一定为回文串
        // dp[i][j] 表示i，j边界组成子串是否为回文串
        public int countSubstrings(String s) {
            boolean[][] dp = new boolean[s.length()][s.length()];


            int result = 0;
            for(int i = dp.length-1; i >= 0; i--){
                for(int j = i; j < dp[0].length; j++){
                    if(s.charAt(i) != s.charAt(j)){
                        dp[i][j] = false;
                    }
                    else {
                        if(j-i  == 0 || j - i == 1){
                            dp[i][j] = true;
                            result ++;
                        }else{
                            System.out.println("i: " + i +  " j :" + j);
                            //使用动态规划向内缩
                            dp[i][j] = dp[i+1][j-1];
                            if(dp[i][j]){
                                result ++;
                            }
                        }
                    }
                }
                System.out.println(Arrays.toString(dp[i]));
            }

            return result;
        }
    }

    @Test
    public void test2(){
        userService.updatePassword(155, "1234", "1234");
    }
}
