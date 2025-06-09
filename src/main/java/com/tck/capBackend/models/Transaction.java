package com.tck.capBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)      //manage the parent foreign key constraint
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // resolve BeanSerialization issue
    Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)      //manage the parent foreign key constraint
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // resolve BeanSerialization issue
    Product product;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "transactType cannot be blank.")
    private EnumTransactType transactType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "status cannot be blank.")
    private EnumStatus status;

    //constructor
    public Transaction(Customer customer, Product product, EnumTransactType transactType, EnumStatus status) {
        this.customer = customer;
        this.product = product;
        this.transactType = transactType;
        this.status = status;
    }

    public Transaction() {

    }

    //getters
    public Integer getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public EnumTransactType getTransactType() {
        return transactType;
    }

    public EnumStatus getStatus() {
        return status;
    }

    public Product getProduct() {
        return product;
    }

    //Setters

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setTransactType(EnumTransactType transactType) {
        this.transactType = transactType;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
