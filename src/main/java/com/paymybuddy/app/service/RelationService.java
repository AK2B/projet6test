package com.paymybuddy.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.dao.RelationRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Relation;

/**
 * Service class for managing user relations.
 */
@Service
public class RelationService {

	@Autowired
	private RelationRepository relationRepository;

	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Adds a relation between the authenticated user and a friend user.
	 *
	 * @param userEmail   The email of the authenticated user.
	 * @param friendEmail The email of the friend user.
	 * @return A string indicating the result of the operation: - "friendNotFound"
	 *         if the friend user does not exist. - "alreadyExist" if the relation
	 *         already exists. - "friendSuccess" if the relation was successfully
	 *         added.
	 */
	public String addRelation(String userEmail, String friendEmail) {
		// Fetch the authenticated user by email
		Customer customerRelation = customerRepository.findByEmail(userEmail);

		// Fetch the friend user by email
		Customer friend = customerRepository.findByEmail(friendEmail);

		if (friend == null) {
			return "friendNotFound";
		}

		// Check if the relation already exists
		if (relationRepository.existsByCustomerRelationAndFriend(customerRelation, friend)) {
			return "alreadyExist";
		}

		// Create a new Relation instance
		Relation relation = new Relation();
		relation.setCustomerRelation(customerRelation); // Set the authenticated user
		relation.setFriend(friend); // Set the friend user

		// Save the relation
		relationRepository.save(relation);

		return "friendSuccess";
	}

	/**
	 * Retrieves a list of relations for a user by their user ID.
	 *
	 * @param userId The user ID for which relations are to be retrieved.
	 * @return A list of Relation objects representing the user's relations.
	 */
	public List<Relation> getRelationForUser(int userId) {
		Customer customer = new Customer();
		customer.setCustomerId(userId);

		return relationRepository.findByCustomerRelation(customer);
	}

	/**
	 * Retrieves a list of email addresses of friends for a user by their user ID.
	 *
	 * @param userId The user ID for which friend emails are to be retrieved.
	 * @return A list of strings representing the email addresses of the user's
	 *         friends.
	 */
	public List<String> getFriendsEmails(int userId) {

		List<String> friendOfEmails = new ArrayList<>();

		List<Relation> relations = getRelationForUser(userId);

		for (Relation relation : relations) {
			Customer friend = relation.getFriend();

			if (friend != null) {
				String email = friend.getEmail();
				friendOfEmails.add(email);
			}
		}

		return friendOfEmails;
	}
}

