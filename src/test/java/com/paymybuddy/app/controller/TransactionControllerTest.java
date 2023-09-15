package com.paymybuddy.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.service.TransactionService;

public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction_Success() {
       
    	Collection<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority("ROLE_USER")); 

    	User user = new User("user@example.com", "password", authorities);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer sender = new Customer();
        sender.setEmail("user@example.com");
        Customer recipient = new Customer();
        recipient.setEmail("recipient@example.com");

        when(customerRepository.findByEmail("user@example.com")).thenReturn(sender);
        when(customerRepository.findByEmail("recipient@example.com")).thenReturn(recipient);

        String result = transactionController.createTransaction(BigDecimal.TEN, "recipient@example.com");

        assertEquals("redirect:/transfer?success", result);

        verify(transactionService).createTransaction(BigDecimal.TEN, sender, recipient);
    }

    @Test
    public void testCreateTransaction_RecipientNotFound() {
        when(customerRepository.findByEmail(anyString())).thenReturn(null);

        String result = transactionController.createTransaction(BigDecimal.TEN, "nonexistent@example.com");

        assertEquals("redirect:/transfer?notFound", result);
    }

    @Test
    public void testCreateTransaction_Fail() {
    	

    	Collection<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority("ROLE_USER")); 
    	
        when(customerRepository.findByEmail("recipient@example.com")).thenReturn(new Customer());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new User("user@example.com", "password", authorities));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        doThrow(new RuntimeException("Transaction failed")).when(transactionService).createTransaction(any(), any(), any());

        String result = transactionController.createTransaction(BigDecimal.TEN, "recipient@example.com");

        assertEquals("redirect:/transfer?fail", result);
    }

}
