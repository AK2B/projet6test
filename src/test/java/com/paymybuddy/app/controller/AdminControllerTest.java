package com.paymybuddy.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.service.TransactionService;

public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private TransactionService transactionService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAdmin() {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setId_transaction(1);
        Transaction transaction2 = new Transaction();
        transaction2.setId_transaction(2);
        transactions.add(transaction1);
        transactions.add(transaction2);

        when(transactionService.getAllTransactions()).thenReturn(transactions);

        String viewName = adminController.GetAdmin(model);

        verify(model).addAttribute("transactions", transactions);

        assertEquals("admin", viewName);
    }
}
