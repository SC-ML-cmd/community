package com.wsc.community;

import com.wsc.community.service.UserService;
import com.wsc.community.dao.DiscussPostMapper;
import com.wsc.community.dao.UserMapper;
import com.wsc.community.entity.DiscussPost;
import com.wsc.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

/*
// Definition for a Node.
class Node {
    public int val;
    public List<Node> children;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }
};
*/
    class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    };
    class Solution {
        // 使用统一遍历的方法
        public List<Integer> postorder(Node root) {
            List<Integer> res = new LinkedList<>();
            if(root == null){
                return res;
            }
            Deque<Node> stack = new LinkedList<>();
            Node node;
            stack.push(root);

            while(!stack.isEmpty()){
                node = stack.peek();
                if(node != null){
                    stack.push(null); //表明当前节点已经遍历过
                    if(node.children.size() > 0){
                        for(int i = root.children.size() - 1; i >= 0; i--){
                            System.out.println("i: " + i);
                            System.out.println("root.children.get(i): " + root.children.get(i).val);
                            stack.push(root.children.get(i));
                        }
                    }

                }
                else{
                    stack.pop();
                    Node cur = stack.pop();
                    res.add(cur.val);
                }
            }
            return res;
        }
    }

    @Test
    public void test2(){
        Node root = new Node(1, new ArrayList<>());
        root.children.add(new Node(3, new ArrayList<>()));
        System.out.println(new Solution().postorder(root));
    }
}
