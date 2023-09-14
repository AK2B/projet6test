package com.paymybuddy.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.service.TransactionService;

@Controller
public class AdminController {

	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping("/admin")
	public String GetAdmin(Model model) {
		
		 Iterable<Transaction> transactions = transactionService.getAllTransactions();
	        model.addAttribute("transactions", transactions);
		return "admin";
	}
	
}
