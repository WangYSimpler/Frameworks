package com.simple.helloDemo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.simple.helloDemo.service.HelloWorldService;

/**
	* @author: WangY
	* @date  : 2016年10月26日 上午11:34:34
*/
public class HelloProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  ApplicationContext context =
	                new ClassPathXmlApplicationContext("beans.xml");
	         
	        HelloWorldService service =
	             (HelloWorldService) context.getBean("helloWorldService");
	          
	        HelloWorld hw= service.getHelloWorld();
	         
	        hw.sayHello();
	}

}
