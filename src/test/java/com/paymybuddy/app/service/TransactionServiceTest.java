package com.paymybuddy.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.dao.TransactionRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Transaction;

@SpringBootTest
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testCreateTransactionValid() {
        // 
        BigDecimal amount = BigDecimal.valueOf(100);
        Customer sender = new Customer();
        sender.setBalance(BigDecimal.valueOf(200));
        Customer recipient = new Customer();
        recipient.setBalance(BigDecimal.valueOf(0));

        // Cas de test : Amount <= 0
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(BigDecimal.ZERO, sender, recipient);
        });

        // Cas de test : Solde insuffisant pour l'expéditeur
        assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(BigDecimal.valueOf(200), sender, recipient);
        });

        // Cas de test : Solde insuffisant pour l'expéditeur (y compris les frais)
        assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(BigDecimal.valueOf(75), sender, recipient);
        });

        
        // mocks
        when(customerRepository.findByEmail("bank@mail.fr")).thenReturn(new Customer());
        when(transactionRepository.save(any())).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            assertNotNull(savedTransaction);
            assertEquals("Completed", savedTransaction.getStatus());
            
            return savedTransaction;
        });

        Transaction transaction = transactionService.createTransaction(amount, sender, recipient);

        assertNotNull(transaction);
        
    }
    
    @Test
    public void testGetTransactionsForUser() {
        int userId = 1;

        // Créez une liste de transactions de test
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setSenderId(userId);
        Transaction transaction2 = new Transaction();
        transaction2.setRecipientId(userId);
        transactions.add(transaction1);
        transactions.add(transaction2);

        // Configurez le comportement attendu pour la transactionRepository.findBySenderIdOrRecipientId
        when(transactionRepository.findBySenderIdOrRecipientId(userId, userId)).thenReturn(transactions);

        // Appelez la méthode à tester
        List<Transaction> result = transactionService.getTransactionsForUser(userId);

        // Assurez-vous que la méthode renvoie la liste de transactions attendue
        assertEquals(2, result.size());
        assertTrue(result.contains(transaction1));
        assertTrue(result.contains(transaction2));
    }

    @Test
    public void testGetAllTransactions() {
        // Créez une liste factice de transactions
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        // Configurez le comportement simulé du repository de transactions
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Appelez la méthode getAllTransactions et vérifiez la taille de la liste
        Iterable<Transaction> result = transactionService.getAllTransactions();
        List<Transaction> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(transactions.size(), resultList.size());
    }

    @Test
    public void testGetCustomerNameForEachTransfer() {
        // Arrange
        int userId = 123; // Replace with the desired user ID
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, new BigDecimal("100.00"), new Timestamp(System.currentTimeMillis()), "Success", 456, userId, null));
        // Add more transactions as needed for your test

        // Mock the behavior of the customerRepository
        when(customerRepository.findById(anyInt())).thenReturn(new Customer(/* Mock customer data */));

        // Act
        List<String> customerNames = transactionService.getCustomerNameForEachTransfer(userId);

        // Assert
        // Perform assertions based on the expected behavior of your method
        assertNotNull(customerNames);
        
    }
   
}

