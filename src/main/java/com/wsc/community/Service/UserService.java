package com.wsc.community.Service;

import com.wsc.community.dao.UserMapper;
import com.wsc.community.entity.User;
import com.wsc.community.util.CommunityConstant;
import com.wsc.community.util.CommunityUtil;
import com.wsc.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.processor.SpringActionTagProcessor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient client;

    @Autowired
    private TemplateEngine engine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();
        //对空值进行判断处理
        if(user == null){
            throw new IllegalArgumentException("参数不能为空!");
        }

        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMessage", "账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMessage", "密码不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMessage", "邮箱不能为空！");
            return map;
        }
        //先验证账号
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMessage", "该账号已经存在");
            return map;
        }

        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null){
            map.put("emailMessage", "该邮箱已存在");
            return map;
        }

        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code;  mybatis会生成id并进行回填
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = engine.process("/mail/activation", context);
        client.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

    //邮箱激活认证的作用： 1.减少恶意注册  2.用户重置密码等信息时使用
    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        if(user.getActivationCode().equals(code)){
            if(user.getStatus() == 0){
                //进行激活
                userMapper.updateStatus(userId, 1);
                return CommunityConstant.ACTIVATION_SUCCESS;
            }else{
                return CommunityConstant.ACTIVATION_REPEAT;
            }
        }else{
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }
}
