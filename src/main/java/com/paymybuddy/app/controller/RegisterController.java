package com.paymybuddy.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.model.Customer;

@Controller
public class RegisterController {

	@Autowired
	private CustomerRepository customerRepository;	
	
	@PostMapping("/sign")
	public String processRegister(@ModelAttribute("customer") Customer customer) {

		Customer savedCustomer = null;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		try {
			String encodedPassword = passwordEncoder.encode(customer.getPassword());
			customer.setPassword(encodedPassword);

			savedCustomer = customerRepository.save(customer);
			
			if (savedCustomer.getCustomerId() > 0) {
				return "redirect:/register?successRegistered";
			}
			return "redirect:/register?registeredFail";
		} catch (Exception ex) {
			return "redirect:/register?exception";
		}
	}
	
	
}