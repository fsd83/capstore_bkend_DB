package com.tck.capBackend.Repository;

import com.tck.capBackend.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    //save()
    //findById(Integer)
    //findAll()
    //count()
    //delete(Customer Object)
    //delete(Integer)

    //Find customer by email containing specific value(s)
    //List<Customer> findByEmailContainingOrLastNameContaining(String email, String lastName);   // SELECT * from customer WHERE email LIKE "%email%" OR lastName LIKE "%lastName%";
    List<Customer> findByEmailContainingOrLastNameContaining(String email, String lastName);
    List<Customer> findByEmailContaining(String email);
    List<Customer> findByLastNameContaining(String lastName);
    Optional<Customer> findByEmail(String username);


}
