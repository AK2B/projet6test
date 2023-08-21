package com.paymybuddy.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.paymybuddy.app.dal.CustomerRepository;
import com.paymybuddy.app.model.Customer;

@Controller
public class ProfileController {

    @Autowired
    private CustomerRepository customerRepository; 

    
    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Check if the principal is an instance of User (OAuth user) or CustomUserDetails (DB user)
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof User) {
            User user = (User) principal;
            String email = user.getUsername();
            
            // Load data from your database
            Customer customerInfo = customerRepository.findByEmail(email);
            
            model.addAttribute("lastName", customerInfo.getLast_name());
            model.addAttribute("firstName", customerInfo.getFirst_name());
            model.addAttribute("email", customerInfo.getEmail());
            model.addAttribute("balance", customerInfo.getBalance());
        }
        
        return "profile";
    }
}
