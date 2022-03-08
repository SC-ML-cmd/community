package com.wsc.community.controller;

import com.wsc.community.Service.UserService;
import com.wsc.community.entity.User;
import com.wsc.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register")
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }


    @RequestMapping(path = "/register" , method = RequestMethod.POST)
    public String register(Model model, User user){
        //注意若页面上的值与User属性名相同，将自动进行注入
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            //注册后，到首页
            //激活后，到登录页面
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱" +
                    "发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMessage", map.get("usernameMessage"));
            model.addAttribute("passwordMessage", map.get("passwordMessage"));
            model.addAttribute("emailMessage", map.get("emailMessage"));
            return "/site/register";
        }
    }

    //注册验证
    //注册后都跳转到中间页面 operate_result页面提示用户
    //注册成功后最终应该跳转到登录页面，注册不成功应该跳转到首页
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        int result = userService.activation(userId, code);
        if(result == ACTIVATION_SUCCESS){
            model.addAttribute("msg", "注册成功，您的账号已经可以正常使用！");
            model.addAttribute("target", "/login");
        } else if(result == ACTIVATION_REPEAT){
            model.addAttribute("msg", "无效操作，该账号已经激活过了！");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败, 您提供的激活码不正确！");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }
}
