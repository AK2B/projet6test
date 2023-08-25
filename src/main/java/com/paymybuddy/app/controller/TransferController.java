package com.paymybuddy.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Relation;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.service.RelationService;
import com.paymybuddy.app.service.TransactionService;

@Controller
public class TransferController {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private RelationService relationService;

	@GetMapping("/transfer")
	public String showTransactionHistory(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = customerRepository.findByEmail(authentication.getName());

		int userId = customer.getCustomerId();

		model.addAttribute("userId", userId);
		
		List<String> friendOfEmails = new ArrayList<>();

		// Récupérer toutes les relations de l'utilisateur connecté
		List<Relation> relations = relationService.getRelationForUser(userId);

		for (Relation relation : relations) {
		    int customerId = relation.getFriendId();

		    // Obtenir l'objet Customer correspondant au customerRelationId
		    Customer friend = customerRepository.findCustomerByCustomerId(customerId);

		    if (friend != null) {
		        String email = friend.getEmail();
		        friendOfEmails.add(email);
		    }
		}

		// Ajouter les adresses e-mail des amis au modèle
		model.addAttribute("friendOfEmails", friendOfEmails);
		
		
		List<Transaction> transactions = transactionService.getTransactionsForUser(userId);

		 model.addAttribute("customerId", userId);
	     model.addAttribute("customerRepository", customerRepository);
		// Ajouter les transactions à la vue
		model.addAttribute("transactions", transactions);

		return "transfer";
	}

}
