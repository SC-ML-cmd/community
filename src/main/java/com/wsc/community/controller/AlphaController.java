package com.wsc.community.controller;

import com.wsc.community.Service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private AlphaService alphaService;


    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "Hello Spring boot.";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }

    //获取请求对象
    //不需要返回值的原因，通过response对象可以直接向浏览器输出数据，不再依赖返回值
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        //请求头
        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " " + value);
        }

        //请求体
        System.out.println(request.getParameter("code"));


        //HttpServletResponse 用于响应http请求，返回响应数据
        response.setContentType("text/html;charset=utf-8");

        try(PrintWriter writer = response.getWriter()){

            writer.println("<h1>牛客网</h1>");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //处理浏览器主要分为两部分接收请求，发出响应，分别是request类  和 response 类

    //GET 请求 适合向服务器获取数据
    //假设查询 /student?current=1&limit=20
    //在服务端都可以通过request.getParameter(参数名)这样的方式来获取。
    // 而@RequestParam注解，就相当于是request.getParameter()，
    // 是从request对象中获取参数的
    @RequestMapping(path = "/student", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    //第二种 从路径中获取参数的方式
    @RequestMapping(path = "student/{id}", method = RequestMethod.GET)
    @ResponseBody //
    public String getStudent( @PathVariable("id")int id){
        System.out.println(id);
        return "a student";
    }

    //POST 请求适合向服务器提交数据  GET请求参数直接显示不安全，GET请求传输的数据有限
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }


    //响应动态HTML
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "张三");
        mav.addObject("age", 30);
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model){  //返回String,表示返回的view对应的路径
        model.addAttribute("name", "北京大学");
        model.addAttribute("age", 100);
        return "/demo/view";
    }

    //服务可以向浏览器中响应HTML数据，以及异步请求 json数据 （当前网页不刷新）
    // json 将java对象 -> JSON字符串 -> JS对象
    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody  //注解为响应体+Map对象，dispatchServlet看到这种情况，将返回JSON对象
    public Map<String, Object> getEmp(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 13);
        map.put("salary", 100000);
        return map;
    }

    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody  //注解为响应体+Map对象，dispatchServlet看到这种情况，将返回JSON对象
    public List<Map<String, Object>> getEmps(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 13);
        map.put("salary", 100000);
        List<Map<String, Object>> lists = new ArrayList<>();

        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "李四");
        map2.put("age", 23);
        map2.put("salary", 100000);
        lists.add(map);
        lists.add(map2);
        return lists;
    }


}
