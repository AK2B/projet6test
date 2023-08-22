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
import lombok.Data;

@Data
@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "customer_id")
	private int customer_id;

	@Column(name = "last_name")
	private String last_name;

	@Column(name = "first_name")
	private String first_name;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "creation_date")
    private LocalDateTime creation_date = LocalDateTime.now(); // Default value using LocalDateTime

    @Column(name = "role")
    private String role = "user";

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	List<Relation> relations = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	List<Transaction> transactions = new ArrayList<>();

	public List<Relation> getrelation() {
		return relations;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

}
