package com.wsc.community.controller;

import com.google.code.kaptcha.Producer;
import com.wsc.community.service.UserService;
import com.wsc.community.entity.User;
import com.wsc.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

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

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session中
        session.setAttribute("kaptcha", text);

        //将图片传给浏览器
        response.setContentType("image/png");

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("响应验证码失败：" + e.getMessage());
        }

    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, HttpSession session, HttpServletResponse response){
        //检查验证码
        String kpatcha = (String) session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kpatcha) || StringUtils.isBlank(code) || !kpatcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg", "验证码不正确！");
            return "/site/login";
        }

        //检查账号密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }
}
