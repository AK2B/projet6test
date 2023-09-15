package com.paymybuddy.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import com.paymybuddy.app.service.RelationService;

public class RelationControllerTest {

	@InjectMocks
    private RelationController relationController;

	@Mock
    private RelationService relationService;

	 @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

    @Test
    public void testAddRelation_FriendNotFound() {
    	Collection<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority("ROLE_USER")); 
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new User("user@example.com", "password",authorities));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(relationService.addRelation("user@example.com", "friend@example.com")).thenReturn("friendNotFound");

        String result = relationController.addRelation("friend@example.com");

        assertEquals("redirect:/transfer?friendNotFound", result);
    }

    @Test
    public void testAddRelation_AlreadyExist() {
    	Collection<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority("ROLE_USER")); 
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new User("user@example.com", "password", authorities));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(relationService.addRelation("user@example.com", "friend@example.com")).thenReturn("alreadyExist");

        String result = relationController.addRelation("friend@example.com");

        assertEquals("redirect:/transfer?alreadyExist", result);
    }

    @Test
    public void testAddRelation_FriendSuccess() {
    	Collection<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority("ROLE_USER")); 
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new User("user@example.com", "password", authorities));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(relationService.addRelation("user@example.com", "friend@example.com")).thenReturn("friendSuccess");

        String result = relationController.addRelation("friend@example.com");

        assertEquals("redirect:/transfer?friendSuccess", result);
    }
}
