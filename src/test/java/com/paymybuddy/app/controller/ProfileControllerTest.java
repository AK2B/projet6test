package com.paymybuddy.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.model.Customer;

public class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowProfile_WithValidUser() {
    	Collection<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority("ROLE_USER")); 
        User user = new User("user@example.com", "password", authorities);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Model model = mock(Model.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Customer customer = new Customer();
        customer.setEmail("user@example.com");
        customer.setLastName("Doe");
        customer.setFirstName("John");
        customer.setBalance(BigDecimal.valueOf(1000));

        when(customerRepository.findByEmail(user.getUsername())).thenReturn(customer);

        String viewName = profileController.showProfile(model, request, response);

        assertEquals("profile", viewName);

        verify(model).addAttribute("lastName", "Doe");
        verify(model).addAttribute("firstName", "John");
        verify(model).addAttribute("email", "user@example.com");
        verify(model).addAttribute("balance", BigDecimal.valueOf(1000));
    }

    @Test
    void testShowProfile_WithInvalidUser() {
    	Collection<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority("ROLE_USER")); 
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken(
                "key", "anonymousUser", authorities));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));

        Model model = new ExtendedModelMap();

        String viewName = profileController.showProfile(model, request, response);

        assertEquals("profile", viewName);

        
    }
}
