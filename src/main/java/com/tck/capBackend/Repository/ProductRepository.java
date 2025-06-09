package com.tck.capBackend.Repository;

import com.tck.capBackend.models.Customer;
import com.tck.capBackend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
