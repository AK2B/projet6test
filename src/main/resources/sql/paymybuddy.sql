DROP DATABASE paymybuddy;

CREATE DATABASE paymybuddy;

USE paymybuddy;




CREATE TABLE `customer`(
   `customer_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `last_name` VARCHAR(50) NOT NULL,
   `first_name` VARCHAR(50) NOT NULL,
   `email` VARCHAR(100) NOT NULL UNIQUE,
   `password` VARCHAR(100) NOT NULL,
   `balance` DECIMAL(10, 2),
   `creation_date` DATE NOT NULL,
   `role` VARCHAR(100) DEFAULT 'USER'
);

INSERT INTO `customer` (`last_name`, `first_name`, `email`, `password`, `balance`, `creation_date`, `role`) 
VALUES ('Zeus', 'Chris', 'bank@mail.fr', '$2a$12$sccCxMaIrbMRK7BMdjP.4ebbzXbTG5GgH0q997ivvQqiv1.IJzed6', 0.00, CURDATE(), 'ADMIN'),
('Doe', 'John', 'john.doe@example.com', '$2a$12$sccCxMaIrbMRK7BMdjP.4ebbzXbTG5GgH0q997ivvQqiv1.IJzed6', 1000.00, CURDATE(), 'USER'), 
('Smith', 'Alice', 'alice.smith@example.com', '$2a$12$cuyV..6Jgld1ofp36j4JXOKzrMSU6YnGj7xippFWb84mQQV0nt.uC', 750.50, CURDATE(), 'USER'), 
('Johnson', 'Bob', 'bob.johnson@example.com', '$2a$12$XDwvAjeQcXwBH4B7CN7HKOG8xWXabXz35Y0Ob.MekN9P6IE4w/N6u', 500.25, CURDATE(), 'USER');




CREATE TABLE `transaction` (
   `transaction_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `amount` DECIMAL(10, 2),
   `description` VARCHAR(150),
   `transaction_date` DATETIME,
   `status` VARCHAR(50),
   `recipient_id` INT NOT NULL,
   `sender_id` INT NOT NULL,
   FOREIGN KEY (recipient_id) REFERENCES customer (customer_id),
   FOREIGN KEY (sender_id) REFERENCES customer (customer_id)
);

INSERT INTO `transaction` (`amount`, `description`, `transaction_date`, `status`, `recipient_id`, `sender_id`)
VALUES (15.50, 'cine', CURDATE(), 'done', 2, 3),
(164.00,'jouet', CURDATE(), 'done', 3, 2),
(5.50, 'eau', CURDATE(), 'done', 1, 3),
(245.50, 'pain', CURDATE(), 'done', 2, 1);




CREATE TABLE `relation` (
   `relation_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `customer_relation_id` INT NOT NULL,
   `friend_id` INT NOT NULL,
   FOREIGN KEY (customer_relation_id) REFERENCES customer (customer_id),
   FOREIGN KEY (friend_id) REFERENCES customer (customer_id)
);


INSERT INTO `relation` (`customer_relation_id`, `friend_id`)
VALUES (1, 2), (2, 3), (2, 1), (3, 2);