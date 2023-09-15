package com.paymybuddy.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.paymybuddy.app.dao.CustomerRepository;
import com.paymybuddy.app.dao.RelationRepository;
import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Relation;

public class RelationServiceTest {

    @InjectMocks
    private RelationService relationService;

    @Mock
    private RelationRepository relationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

    @Test
    public void testAddRelationSuccess() {
        Customer authenticatedUser = new Customer();
        authenticatedUser.setEmail("user@example.com");

        Customer friendUser = new Customer();
        friendUser.setEmail("friend@example.com");

        when(customerRepository.findByEmail("user@example.com")).thenReturn(authenticatedUser);
        when(customerRepository.findByEmail("friend@example.com")).thenReturn(friendUser);
        when(relationRepository.existsByCustomerRelationAndFriend(authenticatedUser, friendUser)).thenReturn(false);

        String result = relationService.addRelation("user@example.com", "friend@example.com");

        assertEquals("friendSuccess", result);
    }

    @Test
    public void testAddRelationFriendNotFound() {
        when(customerRepository.findByEmail("user@example.com")).thenReturn(new Customer());
        when(customerRepository.findByEmail("friend@example.com")).thenReturn(null);

        String result = relationService.addRelation("user@example.com", "friend@example.com");

        assertEquals("friendNotFound", result);
    }

    @Test
    public void testAddRelationAlreadyExist() {
        Customer authenticatedUser = new Customer();
        authenticatedUser.setEmail("user@example.com");

        Customer friendUser = new Customer();
        friendUser.setEmail("friend@example.com");

        when(customerRepository.findByEmail("user@example.com")).thenReturn(authenticatedUser);
        when(customerRepository.findByEmail("friend@example.com")).thenReturn(friendUser);
        when(relationRepository.existsByCustomerRelationAndFriend(authenticatedUser, friendUser)).thenReturn(true);

        String result = relationService.addRelation("user@example.com", "friend@example.com");

        assertEquals("alreadyExist", result);
    }
    

    @Test
    public void testGetRelationForUser() {
        Customer user1 = new Customer();
        user1.setCustomerId(1);

        Customer user2 = new Customer();
        user2.setCustomerId(2);

        Relation relation = new Relation();
        relation.setCustomerRelation(user1);
        relation.setFriend(user2);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation);
        
 
        when(relationRepository.findByCustomerRelation(user1)).thenReturn(relations);

        List<Relation> result = relationService.getRelationForUser(1);           
        
        assertNotNull(result);

        
    }

    @Test
    public void testGetFriendsEmails() {
        
        int userId = 1;
        Customer user = new Customer();
        user.setCustomerId(userId);

        Customer friend1 = new Customer();
        friend1.setEmail("friend1@example.com");

        Customer friend2 = new Customer();
        friend2.setEmail("friend2@example.com");

        List<Relation> relations = new ArrayList<>();
        relations.add(new Relation(1, user, friend1));
        relations.add(new Relation(2, user, friend2));

        RelationService spyRelationService = Mockito.spy(relationService);
        Mockito.when(spyRelationService.getRelationForUser(userId)).thenReturn(relations);

        List<String> friendEmails = spyRelationService.getFriendsEmails(userId);

        assertEquals(2, friendEmails.size());
        assertEquals("friend1@example.com", friendEmails.get(0));
        assertEquals("friend2@example.com", friendEmails.get(1));
    }
}
