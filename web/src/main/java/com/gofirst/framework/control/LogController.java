package com.gofirst.framework.control;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 登录 controller
 * 
 * @author yong
 *
 */
@Controller
public class LogController {

	private static Logger logger = Logger.getLogger(LogController.class);

	/**
	 * 跳转登录页
	 */
	@RequestMapping(value = "toLogin")
	public String toLogin(HttpServletRequest request) {

		return "login";
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(String no, String pwd, HttpServletRequest request, HttpSession session) {
		logger.info("登录成功");

		return "/welcome";
	}
	
	
	
	
	@RequestMapping(value="/logout", method = RequestMethod.DELETE)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		logger.info("退出成功");
	}

	
}
