package com.paymybuddy.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a customer entity with information such as name, email, balance, and transactions.
 *
 * @Entity
 * @Table(name = "customer")
 */
@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "customer_id")
	private int customerId;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "creation_date")
    private LocalDateTime creation_date = LocalDateTime.now(); // Default value using LocalDateTime

    @Column(name = "role")
    private String role = "USER";

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "customerId")
	List<Relation> relations = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "customerId")
	List<Transaction> transactions = new ArrayList<>();

	public List<Relation> getrelation() {
		return relations;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setRelations(int i, Customer user, Customer friend1) {
		// TODO Auto-generated method stub
		
	}

}
