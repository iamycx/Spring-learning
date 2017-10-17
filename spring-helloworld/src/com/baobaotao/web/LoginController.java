package com.baobaotao.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.baobaotao.domain.User;
import com.baobaotao.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/index.html")
	public String loginPage(){
		return "login";
	}
	
	@RequestMapping(value="/loginCheck.html")
	public ModelAndView loginCheck(HttpServletRequest request,LoginCommand loginCommand){
		boolean isVildUser = userService.hasMatchUser(loginCommand.getUserName(), loginCommand.getPassword());
		if(!isVildUser){
			return new ModelAndView("login","error","用户名或密码错误");
		} else{
			User user = userService.findUserByUserName(loginCommand.getUserName());
			user.setLastVisit(new Date());
			user.setLastIp(request.getRemoteAddr());
			userService.loginSuccess(user);
			
			request.getSession().setAttribute("user", user);
			return new ModelAndView("main");
		}
		
	}
}
