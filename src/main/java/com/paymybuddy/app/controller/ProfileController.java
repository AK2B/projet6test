package com.paymybuddy.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.model.Customer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

	@Autowired
	private CustomerRepository customerRepository;

	@GetMapping("/profile")
	public String showProfile(Model model, HttpServletRequest request, HttpServletResponse response) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Check if the principal is an instance of User (OAuth user) or
		// CustomUserDetails (DB user)
		Object principal = authentication.getPrincipal();

		if (principal instanceof User) {
			User user = (User) principal;
			String email = user.getUsername();

			// Load data from your database
			Customer customerInfo = customerRepository.findByEmail(email);

			if (customerInfo != null) {
			    HttpSession session = request.getSession();
			    session.setAttribute("email", customerInfo.getEmail());
			    session.setAttribute("lastName", customerInfo.getLastName());
			    session.setAttribute("firstName", customerInfo.getFirstName());

			    model.addAttribute("lastName", session.getAttribute("lastName"));
			    model.addAttribute("firstName", session.getAttribute("firstName"));
			    model.addAttribute("email", session.getAttribute("email"));
			    model.addAttribute("balance", customerInfo.getBalance());
			}
		}

		return "profile";
	}
}
