package com.paymybuddy.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.app.model.Customer;
import com.paymybuddy.app.model.Relation;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Integer> {

    List<Relation> findByCustomerRelation(Customer customerRelation);

    boolean existsByCustomerRelationAndFriend(Customer customerRelation, Customer friend);

	
}
