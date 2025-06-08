package com.tck.capBackend.Service;

import com.tck.capBackend.models.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerServiceInterface {

    //create method signatures
    public Customer save(Customer customer);
    public List<Customer> findAll();
    public Optional<Customer> findById(Integer id);
    public Customer update(Customer customer);
    public void delete(Integer id);
    public Optional<Customer>findByEmail(String email);

    public List<Customer> findByEmailContainingOrLastNameContaining(String email, String lastName);
    public List<Customer> findByEmailContaining(String email);
    public List<Customer> findByLastNameContaining(String lastName);

}
