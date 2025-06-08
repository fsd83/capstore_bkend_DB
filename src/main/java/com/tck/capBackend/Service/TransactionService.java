package com.tck.capBackend.Service;

import com.tck.capBackend.Repository.TransactionRepository;
import com.tck.capBackend.models.Customer;
import com.tck.capBackend.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements TransactionServiceInterface {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findById(Integer id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteById(Integer id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public long count() {
        return transactionRepository.count();
    }

    public List<Transaction> findAllByCustomer(Customer customer) {
        return transactionRepository.findAllByCustomer(customer);
    }

    public void deleteByCustomer(Integer id) {
        transactionRepository.deleteByCustomerId(id);
    }

}
