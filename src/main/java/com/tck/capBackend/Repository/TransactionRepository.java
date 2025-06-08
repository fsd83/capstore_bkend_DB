package com.tck.capBackend.Repository;

import com.tck.capBackend.models.Customer;
import com.tck.capBackend.models.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    //Derived query
    List<Transaction> findAllByCustomer(Customer customer);

    @Modifying  //essential for custom queries that delete or update records
    @Transactional  //operation must complete within one transaction
    @Query(value="DELETE FROM transaction WHERE customer_id = :id", nativeQuery = true)
    void deleteByCustomerId(@Param("id") Integer id);
}
