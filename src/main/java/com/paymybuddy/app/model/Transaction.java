package com.paymybuddy.app.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_transaction")
	private int id_transaction;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "transaction_date")
	private Timestamp transaction_date;

	@Column(name = "status")
	private String status;

	@Column(name = "recipient_id")
	private int recipientId;

	@Column(name = "sender_id")
	private int senderId;

	public Transaction() {
	}

	public Transaction(int id_transaction, BigDecimal amount, Timestamp transaction_date, String status,
			int recipientId, int senderId, Customer customer) {
		super();
		this.id_transaction = id_transaction;
		this.amount = amount;
		this.transaction_date = new Timestamp(System.currentTimeMillis());
		this.status = status;
		this.recipientId = recipientId;
		this.senderId = senderId;
		
	}

	
}
