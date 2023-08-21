package com.paymybuddy.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.app.dal.CustomerRepository;
import com.paymybuddy.app.dal.RelationRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Relation;

@Controller
public class RelationController {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
	private RelationRepository relationRepository;

    @PostMapping("/add-relation")
    public ResponseEntity<String> addRelation(@RequestParam("friendEmail") String friendEmail) {
        // Retrieve the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Customer customerRelationId = customerRepository.findByEmail(user.getUsername());

        // Fetch the friend user by email
        Customer friend = customerRepository.findByEmail(friendEmail);

        // Check if the relation already exists
        if (relationRepository.existsByCustomerRelationIdAndFriendId(customerRelationId.getCustomer_id(), friend.getCustomer_id())) {
            return ResponseEntity.badRequest().body("Relation already exists.");
        }

        // Create a new Relation instance
        Relation relation = new Relation();
        relation.setCustomerRelationId(customerRelationId.getCustomer_id());  // Set the authenticated user
        relation.setFriendId(friend.getCustomer_id());   // Set the friend user

        // Save the relation
        relationRepository.save(relation);

        return ResponseEntity.ok("Relation successful.");
    }

}