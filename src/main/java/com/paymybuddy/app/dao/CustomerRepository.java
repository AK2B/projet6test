package com.paymybuddy.app.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.app.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	List<Customer> findAllByEmail(String email);
	
	Customer findByEmail(String email);

	Customer findCustomerByCustomerId(int customerId);

	Customer findById(int customerId);
	
}
