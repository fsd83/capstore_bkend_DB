package com.tck.capBackend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key, auto-incremented
    Integer Id;

    @Column(nullable = false)
    @NotBlank(message = "name must not be blank.")
    @Size(min=3, message = "Min 3 characters for name.")

    String name;

    @Column(nullable = false)
    @NotBlank(message = "Product description cannot be blank.")
    String description;

    @Column(nullable = false)
    @NotBlank(message = "imagePath must not be blank.")
    @Size(min=3, message = "Min 3 characters for the imagePath.")
    private String imagePath; // Store the path to the image

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "productType cannot be blank.")
    private EnumProductType productType;

    public Product(String name, String description, String imagePath, EnumProductType productType) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.productType = productType;
    }

    public Product() {
    }

    //getters
    public Integer getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public EnumProductType getProductType() {
        return productType;
    }

    //setters

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setProductType(EnumProductType productType) {
        this.productType = productType;
    }

}
