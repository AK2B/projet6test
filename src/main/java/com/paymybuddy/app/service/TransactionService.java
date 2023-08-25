package com.paymybuddy.app.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.dao.TransactionRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Transaction;

@Service
public class TransactionService {
	
	private static final BigDecimal TRANSACTION_FEE_PERCENTAGE = BigDecimal.valueOf(0.005); // 0.5%

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Transaction createTransaction(BigDecimal amount, Customer sender, Customer recipient) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance for the sender");
        }

        BigDecimal transactionFee = amount.multiply(TRANSACTION_FEE_PERCENTAGE);
        BigDecimal totalAmountWithFee = amount.add(transactionFee);

        // Ensure sender has enough balance after deducting the total amount with fee
        if (sender.getBalance().compareTo(totalAmountWithFee) < 0) {
            throw new RuntimeException("Insufficient balance for the sender (including fee)");
        }

        // Update sender's balance after deducting the total amount with fee
        sender.setBalance(sender.getBalance().subtract(totalAmountWithFee));

        // Update recipient's balance
        recipient.setBalance(recipient.getBalance().add(amount));

        // Perform the transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransaction_date(new Timestamp(System.currentTimeMillis()));
        transaction.setStatus("Completed");
        transaction.setSenderId(sender.getCustomerId());
        transaction.setRecipientId(recipient.getCustomerId());

        transactionRepository.save(transaction);
        customerRepository.save(sender);
        customerRepository.save(recipient);

        // Collect transaction fee from sender and credit it to the third party (bank)
        Customer thirdParty = customerRepository.findByEmail("bank@mail.fr");
        if (thirdParty != null) {
            thirdParty.setBalance(thirdParty.getBalance().add(transactionFee));
            customerRepository.save(thirdParty);
        } else {
            throw new RuntimeException("Third party with email bank@mail.fr not found");
        }

        return transaction;
    }
    
    public List<Transaction> getTransactionsForUser(int userId) {
        // Récupérer les transactions pour l'utilisateur donné
        return transactionRepository.findBySenderIdOrRecipientId(userId, userId);
    }

}
