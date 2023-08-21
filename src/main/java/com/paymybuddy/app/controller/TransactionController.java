package com.paymybuddy.app.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.app.dal.CustomerRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@RequestParam BigDecimal amount,
                                                    @RequestParam String recipientEmail) {
        try {
            Customer recipient = customerRepository.findByEmail(recipientEmail);
            if (recipient == null) {
                return ResponseEntity.badRequest().body("Recipient not found.");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Customer sender = customerRepository.findByEmail(user.getUsername());

            // Create the transaction using sender and recipient
            Transaction transaction = transactionService.createTransaction(amount, sender, recipient);

            return ResponseEntity.ok("Transaction successful. Transaction ID: " + transaction.getId_transaction());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction failed: " + e.getMessage());
        }
    }
}
