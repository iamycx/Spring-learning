package com.iamycx.club.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by iamycx on 11:06 2017/12/19
 */
//@Controller
public class IndexController {
    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index(HttpSession httpSession) {
        return "Hello " + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "zz", required = false) String key) {
        return String.format("Profile page of %s / %d, type:%d  key:%s", groupId, userId, type, key);
    }

    @RequestMapping(path = {"/vm"})
    public String temp(Model model) {
        model.addAttribute("value1","ycxycx");
        List<String> colors = Arrays.asList(new String[]{"red","bule","green"});
        model.addAttribute("colors",colors);
        Map<String,String> map = new HashMap<>();
        for (int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);
        return "home";
    }

    @RequestMapping(path = {"/request"},method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response, HttpServletRequest request, HttpSession httpSession) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }
        if (request.getCookies()!=null){
            for(Cookie cookie : request.getCookies()){
                sb.append("Cookie:"+cookie.getName()+" Value:"+cookie.getValue());
            }
        }
        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        sb.append(request.getRequestURL()+"<br>");
        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"},method = {RequestMethod.GET})
    public String redirect(@PathVariable("code") int code,HttpSession httpSession) {
        httpSession.setAttribute("msg","I am from redirect");
        return "redirect:/";
    }
}
