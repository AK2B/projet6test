package com.paymybuddy.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.model.Customer;

@RunWith(MockitoJUnitRunner.class)
public class RegisterControllerTest {

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessRegister_Success() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("johndoe@example.com");
        customer.setPassword("password");

        Customer savedCustomer = new Customer();
        savedCustomer.setCustomerId(1); 

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        String result = registerController.processRegister(customer);

        assertEquals("redirect:/register?successRegistered", result); 
    }

    

    @Test
    public void testProcessRegister_Failure() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("johndoe@example.com");
        customer.setPassword("password");

        when(customerRepository.save(any(Customer.class))).thenReturn(null);

        String result = registerController.processRegister(customer);

        assertEquals("redirect:/register?exception", result); 
    }


    @Test
    public void testProcessRegister_Exception() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("johndoe@example.com");
        customer.setPassword("password");

        when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException("Test exception"));

        String result = registerController.processRegister(customer);

        assertEquals("redirect:/register?exception", result);
    }
}
