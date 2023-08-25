package com.paymybuddy.app.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.service.TransactionService;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/transactions/create")
    public String createTransaction(@RequestParam BigDecimal amount,
                                    @RequestParam String recipientEmail) {
        try {
            Customer recipient = customerRepository.findByEmail(recipientEmail);
            if (recipient == null) {       
                return "redirect:/transfer?notFound";
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Customer sender = customerRepository.findByEmail(user.getUsername());

            // Create the transaction using sender and recipient
            transactionService.createTransaction(amount, sender, recipient);
           
            return "redirect:/transfer?success";
        } catch (Exception e) {
            return "redirect:/transfer?fail";
        }
    }
}
