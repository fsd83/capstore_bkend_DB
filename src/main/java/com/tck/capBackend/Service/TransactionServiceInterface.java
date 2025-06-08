package com.tck.capBackend.Service;

import com.tck.capBackend.models.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionServiceInterface {

    // create method signatures
    public Transaction saveTransaction(Transaction transaction);

    public List<Transaction> findAll();

    public Optional<Transaction>findById(Integer id);

    public Transaction updateTransaction(Transaction transaction);

    public void deleteById(Integer id);

    public  long count();
}
