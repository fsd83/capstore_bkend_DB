package com.tck.capBackend.Service;

import com.tck.capBackend.Repository.ProductRepository;
import com.tck.capBackend.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements ProductServiceInterface{

    @Autowired
    ProductRepository productRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product save(Product product, MultipartFile imageFile) throws IOException {

        // Create a file structure to upload to uploads/images/filename.jpg
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        File img = new File(uploadDir + File.separator + fileName);
        imageFile.transferTo(img.toPath());
        product.setImagePath(String.format("/%s/%s", uploadDir, fileName));

        return productRepository.save(product);
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
