package com.paymybuddy.app.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.service.RelationService;
import com.paymybuddy.app.service.TransactionService;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private RelationService relationService;

    @Test
    public void testShowTransactionHistory() throws Exception {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = new Customer();
        customer.setCustomerId(123);
        when(customerRepository.findByEmail("user@example.com")).thenReturn(customer);

        List<String> friendOfEmails = new ArrayList<>();
        when(relationService.getFriendsEmails(123)).thenReturn(friendOfEmails);

        List<Transaction> transactions = new ArrayList<>();
        when(transactionService.getTransactionsForUser(123)).thenReturn(transactions);

        List<String> customerNames = new ArrayList<>();
        when(transactionService.getCustomerNameForEachTransfer(123)).thenReturn(customerNames);

        // Act and Assert
        mockMvc.perform(get("/transfer").with(user("user@example.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeExists("userId", "friendOfEmails", "transactions", "customerNames"))
                .andExpect(model().attribute("userId", 123))
                .andExpect(model().attribute("friendOfEmails", friendOfEmails))
                .andExpect(model().attribute("transactions", transactions))
                .andExpect(model().attribute("customerNames", customerNames));

        // Verify that the methods were called as expected
        verify(customerRepository, times(1)).findByEmail("user@example.com");
        verify(relationService, times(1)).getFriendsEmails(123);
        verify(transactionService, times(1)).getTransactionsForUser(123);
        verify(transactionService, times(1)).getCustomerNameForEachTransfer(123);
    }
}
