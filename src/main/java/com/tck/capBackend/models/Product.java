package com.tck.capBackend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

//  YAN - to set validation of image file
    @Column
    @NotBlank(message = "Image path is required")
    @Size(max = 255, message = "Image path must be less than 255 characters")
    @Pattern(
            regexp = "([^\s]+(\\.(?i)(jpg|jpeg|png))$)",
            message = "Please use a valid image (.jpg, .jpeg, .png)."
    )
    private String imagePath;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "productType cannot be blank.")
    private EnumProductType productType;

    //YAN
    private String imageName;
    private String imageType;
    //to store data?
    @Lob
    private byte[] imageData;


    public Product(String name, String description, String imagePath, EnumProductType productType, String imageName, String imageType, byte[] imageData) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath; // YAN - or imageType?
        this.productType = productType;

        //images
        this.imageName = imageName;
        this.imageType = imageType;
        this.imageData = imageData;
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

    public String getImageName() { return imageName; }

    public String getImageType() { return imageType; }

    public byte[] getImageData() { return imageData; }

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

    public void setImageName(String imageName) { this.imageName = imageName;}

    public void setImageType(String imageType) { this.imageType = imageType;}

    public void setImageData(byte[] imageData) { this.imageData = imageData; }

}
