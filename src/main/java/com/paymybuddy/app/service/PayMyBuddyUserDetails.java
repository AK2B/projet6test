package com.paymybuddy.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.model.Customer;

@Service
public class PayMyBuddyUserDetails implements UserDetailsService {

	@Autowired
	CustomerRepository customerRepository;
	 

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		String eMail, password = null;
		
		List<GrantedAuthority> authorities = null;
		Customer customer = customerRepository.findByEmail(email);
		if (customer == null) {
			throw new UsernameNotFoundException("User details not found for the user : " + email);
		} else {
			eMail = customer.getEmail();
			password = customer.getPassword();
			authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(customer.getRole()));
		}
		return new User(email, password, authorities);
	}

}
