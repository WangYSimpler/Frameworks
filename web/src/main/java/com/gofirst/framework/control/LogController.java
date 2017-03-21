package com.gofirst.framework.control;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gofirst.framework.bean.UserForm;


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
	 * 
	 * @param no
	 * @param pwd
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public  ModelAndView login(String no, String pwd, HttpServletRequest request, HttpSession session) {
		logger.info("登录成功");

		return new ModelAndView("index");
	}
	
	
	@RequestMapping(value="/test",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> testPostJson(@RequestBody  UserForm userForm,BindingResult bindingResult) {
			
		Map<String, Object> map = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			map.put("errorCode", "40001");
			map.put("errorMsg", bindingResult.getFieldError().getDefaultMessage());
		}
		
		map.put("user", userForm);
		return map;
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.DELETE)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		logger.info("退出成功");
	}

	
}
