package com.tck.capBackend.Service;

import com.tck.capBackend.Exception.ResourceNotFoundException;
import com.tck.capBackend.Repository.ProductRepository;
import com.tck.capBackend.models.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements ProductServiceInterface{

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }
    //YAN - adding product to database. Is it under save(Product) or add(Product)
    @Override
    public Product addProduct(@Valid Product product, MultipartFile imageFile) throws IOException {
        //product.setName(imageFile.getOriginalFilename()); //Is this name for image or for overall product?
        product.setImagePath(imageFile.getContentType()); //what about imageType?
        product.setImageType(imageFile.getContentType());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageData(imageFile.getBytes());
        return productRepository.save(product);
    }

    //YAN - need this? or use the findById will do?
    @Override
    public Product getProductById(Integer id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public long count() {
        return productRepository.count();
    }


}
