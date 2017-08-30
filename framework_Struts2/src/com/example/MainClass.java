package com.example;

import org.apache.naming.resources.Resource;

import com.sun.net.httpserver.HttpServer;

public class MainClass {
	
	public static final String BASE_URI = "http://localhost:8888/myapp/";
	public static HttpServer startServer(){
		final ResourceConfig rc = new ResourceConfig().packages("com.example");
	}
	
	public static void main(String[] args) {
		
	}

}
