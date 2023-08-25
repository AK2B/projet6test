package com.paymybuddy.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.app.dao.RelationRepository;
import com.paymybuddy.app.model.Relation;

@Service
public class RelationService {

	@Autowired
	private RelationRepository relationRepository;
	
	public List<Relation> getRelationForUser(int userId) {
        
        return relationRepository.findByCustomerRelationId(userId);
    }
}
