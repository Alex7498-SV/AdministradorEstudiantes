package com.student.administrador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.student.administrador.services.TodoService;


@Controller
public class MainController {

	@Autowired
	private TodoService service;
	
	@RequestMapping("/index")
	public ModelAndView initMain(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");
		return mav;
	}
	
}
