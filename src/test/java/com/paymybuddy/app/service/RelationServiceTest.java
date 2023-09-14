package com.paymybuddy.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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
        // Create a user with a specific customerId
        Customer user1 = new Customer();
        user1.setCustomerId(1);

        Customer user2 = new Customer();
        user2.setCustomerId(2);

        // Create a relation with the user
        Relation relation = new Relation();
        relation.setCustomerRelation(user1);
        relation.setFriend(user2);

        // Create a list of relations to return when mocked
        List<Relation> relations = new ArrayList<>();
        relations.add(relation);
        
 
        // Mock relationRepository.findByCustomerRelation to return the relations
        when(relationRepository.findByCustomerRelation(user1)).thenReturn(relations);

        // Call the service method to get relations for user 1
        List<Relation> result = relationService.getRelationForUser(1);           
        
        // Verify that the result is not null
        assertNotNull(result);

        
    }

    @Test
    public void testGetFriendsEmails() {
        
        // Créez un utilisateur factice avec un ID
        int userId = 1;
        Customer user = new Customer();
        user.setCustomerId(userId);

        // Créez quelques amis factices avec des adresses e-mail
        Customer friend1 = new Customer();
        friend1.setEmail("friend1@example.com");

        Customer friend2 = new Customer();
        friend2.setEmail("friend2@example.com");

        // Créez une liste factice de relations avec les amis
        List<Relation> relations = new ArrayList<>();
        relations.add(new Relation(1, user, friend1));
        relations.add(new Relation(2, user, friend2));

        // Utilisez Mockito pour simuler le comportement de getRelationForUser
        RelationService spyRelationService = Mockito.spy(relationService);
        Mockito.when(spyRelationService.getRelationForUser(userId)).thenReturn(relations);

        // Appelez la méthode getFriendsEmails
        List<String> friendEmails = spyRelationService.getFriendsEmails(userId);

        // Vérifiez que la liste des e-mails d'amis contient les adresses attendues
        assertEquals(2, friendEmails.size());
        assertEquals("friend1@example.com", friendEmails.get(0));
        assertEquals("friend2@example.com", friendEmails.get(1));
    }
}
