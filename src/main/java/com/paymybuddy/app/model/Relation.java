package com.paymybuddy.app.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "relation")
public class Relation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator="native")
	@GenericGenerator(name="native",strategy="native")
    @Column(name = "relation_id")
    private int relation_id;

	@OneToOne
    @JoinColumn(name = "customer_relation_id", referencedColumnName = "customer_id")
    private Customer customerRelation;

    @OneToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "customer_id")
    private Customer friend; 


    public Relation() {
    }

    public Relation(int relation_id, Customer customerRelation, Customer friend) {
        super();
    	this.relation_id = relation_id;
        this.customerRelation = customerRelation;
        this.friend = friend;
    }
}
