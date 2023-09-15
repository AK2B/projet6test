package com.paymybuddy.app.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.dao.TransactionRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Transaction;

/**
 * This service class handles transactions between customers, including fee calculation
 * and balance updates.
 *
 */
@Service
public class TransactionService {
	
    // Transaction fee percentage (0.5%)
    private static final BigDecimal TRANSACTION_FEE_PERCENTAGE = BigDecimal.valueOf(0.005);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Creates a transaction between two customers.
     *
     * @param amount    The transaction amount.
     * @param sender    The sender of the transaction.
     * @param recipient The recipient of the transaction.
     * @return The created transaction.
     * @throws IllegalArgumentException if the amount is not greater than zero.
     * @throws RuntimeException         if there's insufficient balance or other errors.
     */
    @Transactional
    public Transaction createTransaction(BigDecimal amount, Customer sender, Customer recipient) {
        // Check if the amount is greater than zero
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // Check if the sender has sufficient balance
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance for the sender");
        }

        // Calculate transaction fee and total amount with fee
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

        // Create and save the transaction
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
    
    /**
     * Retrieves all transactions for a user with the given user ID.
     *
     * @param userId The ID of the user.
     * @return A list of transactions for the user.
     */
    public List<Transaction> getTransactionsForUser(int userId) {
        return transactionRepository.findBySenderIdOrRecipientId(userId, userId);
    }
    
    /**
     * Retrieves all transactions.
     *
     * @return An iterable of all transactions.
     */
    public Iterable<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    /**
     * Retrieves the customer names involved in transactions for a user with the given user ID.
     *
     * @param userId The ID of the user.
     * @return A list of customer names involved in the user's transactions.
     */
    public List<String> getCustomerNameForEachTransfer(int userId){
        List<Transaction> transactions = getTransactionsForUser(userId);
        List<String> customerNames = new ArrayList<>();
        for (Transaction transaction : transactions) {
            int displayId = transaction.getSenderId() != userId ? transaction.getSenderId()
                    : transaction.getRecipientId();

            Customer customerFind = customerRepository.findById(displayId);

            String customerName = "";
            if (customerFind != null) {
                customerName = customerFind.getLastName() + " " + customerFind.getFirstName();
            }

            customerNames.add(customerName);
        }
        return customerNames;
    }
}
