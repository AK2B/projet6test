package com.paymybuddy.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NavigationController {
	
	@RequestMapping(value = {"/home", "/"})
	public String getHome() {
		return "home";
	}
	
	@RequestMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@RequestMapping("/register")
	public String getRegister() {
		return "register";
	}
	
	@RequestMapping("/contact")
	public String getContact() {
		return "contact";
	}

}
