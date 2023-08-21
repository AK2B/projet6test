package com.paymybuddy.app.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.app.model.Relation;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Integer>{

	List<Relation> findByCustomerRelationId(int customerRelationId);

	 boolean existsByCustomerRelationIdAndFriendId(int customerRelationId, int friendId);

	
}
