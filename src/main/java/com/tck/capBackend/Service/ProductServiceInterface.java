package com.tck.capBackend.Service;

import com.tck.capBackend.models.Customer;
import com.tck.capBackend.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductServiceInterface {

    //create method signatures
    public Product save(Product product);
    public Product save(Product product, MultipartFile imageFile) throws IOException;
    public List<Product> findAll();
    public Optional<Product> findById(Integer id);
    public Product update(Product product);
    public void deleteById(Integer id);
    public  long count();

}
