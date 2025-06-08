package com.tck.capBackend.Controller;

import com.tck.capBackend.Exception.ResourceNotFoundException;
import com.tck.capBackend.Service.CustomerService;
import com.tck.capBackend.Service.TransactionService;
import com.tck.capBackend.models.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/user/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    CustomerService customerService;

    @PostMapping("/transaction/add")      // customer_id rep. which customer saved the transaction
    public ResponseEntity<Object> saveTransaction(
            @Valid @RequestBody Transaction transaction) throws ResourceNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByEmail(authentication.getName()).map((_customer)->{
                //Frontend need to ensure that transaction with following parameters are
                //never send to here:
                // 1) (EnumTransactType.DONATE && EnumStatus.IN_PROGRESS)
                // 2) (EnumRole.USER && EnumStatus.COMPLETED)
                  Transaction _transaction = new Transaction(_customer, transaction.getTransactType(), transaction.getStatus());
                  return transactionService.saveTransaction(_transaction);

                }).orElseThrow(() -> new ResourceNotFoundException("Customer not found.")).getCustomer();

            return new ResponseEntity<>(customer, HttpStatus.OK);

    }

    //find all transaction
    @GetMapping("/admin/transaction/all")
    public ResponseEntity<Object> allTransaction() throws ResourceNotFoundException{

            List<Transaction> transactionList = transactionService.findAll();
            if(transactionList.isEmpty())
                throw new ResourceNotFoundException("No transaction found");
            return new ResponseEntity<>(transactionList, HttpStatus.OK);

    }

    //update transaction
    @PutMapping("/admin/transaction/{transaction_id}")
    public ResponseEntity<Object> updateTransaction(
            @PathVariable("transaction_id") Integer transaction_id,
            @Valid @RequestBody Transaction transaction) throws ResourceNotFoundException{

            Transaction checkTransaction = transactionService.findById(transaction_id).map((_transaction) -> {
                _transaction.setStatus(transaction.getStatus());
                return transactionService.saveTransaction(_transaction);
            }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found."));

            return new ResponseEntity<>(checkTransaction, HttpStatus.OK);
    }

    //delete transaction
    @DeleteMapping("/admin/transaction/{transaction_id}")
    public ResponseEntity<Object> deleteTransaction(
            @PathVariable("transaction_id") Integer transaction_id) throws ResourceNotFoundException{

            //find the transaction to delete
            Transaction checkTransaction = transactionService.findById(transaction_id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found."));
            transactionService.deleteById(transaction_id);

            return new ResponseEntity<>(checkTransaction, HttpStatus.OK);
    }

    //COUNT transaction
    @GetMapping("/admin/count")
    public ResponseEntity<Object> countTransaction() {
            return new ResponseEntity<>(transactionService.count(), HttpStatus.OK);
    }

    //Delete transaction from one customer
    @DeleteMapping("/admin/customer/{customer_id}")
    public ResponseEntity<Object> deleteTransactionByCustomer(
            @PathVariable("customer_id") Integer customer_id) throws ResourceNotFoundException{

            Customer customer = customerService.findById(customer_id).map((_customer)->{
                transactionService.deleteByCustomer(_customer.getId());
                return _customer;
            }).orElseThrow(()-> new ResourceNotFoundException("There was an error"));

            String response = String.format("All Transactions Deleted for customer id %d.", customer.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Find all transaction from one customer
    @GetMapping("/getCustomer/{customer_id}")
    public ResponseEntity<Object> getAllTransactionByCustomer(
            @PathVariable("customer_id") Integer customer_id) throws ResourceNotFoundException{

            //Frontend need to ensure that if customer role = USER,
            //customer can only access his own transaction record
            List<Transaction> getList = customerService.findById(customer_id).map((_customer)->{
                return transactionService.findAllByCustomer(_customer);

            }).orElseThrow(()-> new ResourceNotFoundException("Cannot find customer"));

            return new ResponseEntity<>(getList, HttpStatus.OK);

    }

}
