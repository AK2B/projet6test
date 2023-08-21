package com.paymybuddy.app.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "relation")
public class Relation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator="native")
	@GenericGenerator(name="native",strategy="native")
    @Column(name = "relation_id")
    private int relation_id;

    @Column(name = "customer_relation_id")
    private int customerRelationId;

    @Column(name = "friend_id")
    private int friendId;

    public Relation() {
    }

    public Relation(int relation_id, int customerRelationId, int friendId) {
        super();
    	this.relation_id = relation_id;
        this.customerRelationId = customerRelationId;
        this.friendId = friendId;
    }
}
