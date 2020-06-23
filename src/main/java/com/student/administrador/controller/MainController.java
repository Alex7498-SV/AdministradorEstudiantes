package com.student.administrador.controller;

import javax.xml.ws.RequestWrapper;

import org.springframework.web.bind.annotation.RequestMapping;

public class MainController {

	@RequestMapping("/login")
	String login() {
		return "login";
	}
	
}
