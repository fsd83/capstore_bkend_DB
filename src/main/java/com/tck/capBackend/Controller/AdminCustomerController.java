package com.tck.capBackend.Controller;

import com.tck.capBackend.Exception.ResourceNotFoundException;
import com.tck.capBackend.Service.CustomerService;
import com.tck.capBackend.models.Customer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminCustomerController {
    @Autowired
    CustomerService customerService;

    //CustomerRepository customerRepository;

    //in replacement of @Autowired
//    public CustomerController(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }

    @PostMapping("/admin/add")
    public ResponseEntity<Object> addCustomer(@Valid @RequestBody Customer customer) throws Exception{

        // save the customer to the database
        // the password for the customer need to be encoded first
        return new ResponseEntity<>(customerService.save(customer), HttpStatus.CREATED);

    }

    @GetMapping("/admin/all")
    public ResponseEntity<Object> allCustomers() throws ResourceNotFoundException{

            //retrieve all customers
            List<Customer> customers = (List<Customer>) customerService.findAll();
            if(customers.isEmpty()){
                throw new ResourceNotFoundException("No customer found.");
            }

            return new ResponseEntity<>(customers, HttpStatus.OK);

    }

    @PutMapping("/admin/update/{id}") // Path variable
    public ResponseEntity<Object> updateCustomer(@PathVariable("id") Integer id, @Valid @RequestBody Customer customer) throws ResourceNotFoundException {

        // find the customer, else throw custom exception ResourceNotFoundException
        Customer currentCustomer = customerService
                .findById(id)
                .map(_customer->{
                    _customer.setFirstName(customer.getFirstName());
                    _customer.setLastName(customer.getLastName());
                    _customer.setEmail(customer.getEmail());
                    _customer.setPhone(customer.getPhone());
                    _customer.setPassword(customer.getPassword());
                    return customerService.update(_customer);

                }).orElseThrow(()->new ResourceNotFoundException("Customer not found."));

        return new ResponseEntity<>(currentCustomer, HttpStatus.OK); // or use NO_CONTENT

    }

    @GetMapping("/admin/getInfo/{id}")    //path variable
    public ResponseEntity<Object> getCustomerById(@PathVariable("id") Integer id) throws ResourceNotFoundException {

            //Get the customer by Id and return the response
            Customer customer = customerService.findById(id).orElseThrow(()->new ResourceNotFoundException("Customer not found."));

            return new ResponseEntity<>(customer, HttpStatus.OK); //200

    }

    @DeleteMapping("/admin/{id}")    //path variable
    public ResponseEntity<Object> deleteCustomerById(@PathVariable("id") Integer id) throws ResourceNotFoundException {

            //Get the customer by Id and return the response
            Customer customer = customerService.findById(id).orElseThrow(()->new ResourceNotFoundException("Unable to perform the task."));
            customerService.delete(customer.getId());

            return new ResponseEntity<>(customer, HttpStatus.OK); //200

    }

    @GetMapping("/restricted")
    public ResponseEntity<Object> getCustomerByEmailOrLastName(
            @RequestParam("email") String email,                            // email is a url param
            @RequestParam("lastName") String lastName) throws ResourceNotFoundException{    // lastName is a url param

            if(!email.isBlank() && !lastName.isBlank()) {

                List<Customer> customers = customerService.
                        findByEmailContainingOrLastNameContaining(email, lastName);

                if (customers.isEmpty())
                    throw new ResourceNotFoundException("Customer not found.");

                return new ResponseEntity<>(customers, HttpStatus.OK);

            }else if(!email.isBlank() && lastName.isBlank()){

                List<Customer> customers = customerService.findByEmailContaining(email);

                if (customers.isEmpty())
                    throw new ResourceNotFoundException("Customer not found.");

                return new ResponseEntity<>(customers, HttpStatus.OK);

            }else if(email.isBlank() && !lastName.isBlank()) {

                List<Customer> customers = customerService.findByLastNameContaining(lastName);

                if (customers.isEmpty())
                    throw new ResourceNotFoundException("Customer not found.");

                return new ResponseEntity<>(customers, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(allCustomers(), HttpStatus.OK);

            }

    }



//    @GetMapping("")
//    public ResponseEntity<Object> getCustomerByEmailOrLastName(
//            @RequestParam("email") String email,                            // email is a url param
//            @RequestParam("lastName") String lastName) throws Exception
//    {    // lastName is a url param
//
//        try {
//
//            if(!email.isBlank() && !lastName.isBlank()) {
//
//                List<Customer> customers = customerRepository.
//                        findByEmailContainingOrLastNameContaining(email, lastName);
//
//                if (customers.isEmpty())
//                    throw new Exception("Customer not found.");
//
//                return new ResponseEntity<>(customers, HttpStatus.OK);
//
//            }else if(!email.isBlank() && lastName.isBlank()){
//
//                List<Customer> customers = customerRepository.findByEmailContaining(email);
//
//                if (customers.isEmpty())
//                    throw new Exception("Customer not found.");
//
//                return new ResponseEntity<>(customers, HttpStatus.OK);
//
//            }else{
//
//                List<Customer> customers = customerRepository.findByLastNameContaining(lastName);
//
//                if (customers.isEmpty())
//                    throw new Exception("Customer not found.");
//
//                return new ResponseEntity<>(customers, HttpStatus.OK);
//            }
//
//
//        }catch(Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }



}
