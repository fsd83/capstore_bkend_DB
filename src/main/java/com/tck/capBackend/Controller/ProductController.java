package com.tck.capBackend.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tck.capBackend.Exception.ResourceNotFoundException;
import com.tck.capBackend.Repository.CustomerRepository;
import com.tck.capBackend.Repository.ProductRepository;
import com.tck.capBackend.Service.CustomerService;
import com.tck.capBackend.Service.ProductService;
import com.tck.capBackend.Service.TransactionService;
import com.tck.capBackend.models.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.util.List;

@RestController
@RequestMapping("/user/product")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CustomerService customerService;

    @Autowired
    TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<Object> addProduct(@Valid @RequestBody Product product) throws Exception{

        // 1. Find the USER
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByEmail(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        // 2. Save the product
        Product savedProduct = productService.save(product);

        // 3. Bridge the user and product, save the transaction (DONATION)
        Transaction _transaction = new Transaction(customer, savedProduct, EnumTransactType.DONATE, EnumStatus.AVAILABLE);

        return new ResponseEntity<>(transactionService.saveTransaction(_transaction), HttpStatus.CREATED);

    }

    @PostMapping("/addwithimage")
    public ResponseEntity<Object> addProductWithImage(@Valid @RequestPart("product") String data, @RequestPart("image") MultipartFile imageFile) throws Exception{

        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(data, Product.class);

        // 1. Find the USER
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByEmail(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

//         2. Save the product
        Product savedProduct = productService.save(product, imageFile);

        // 3. Bridge the user and product, save the transaction (DONATION)
        Transaction _transaction = new Transaction(customer, product, EnumTransactType.DONATE, EnumStatus.AVAILABLE);

        return new ResponseEntity<>(transactionService.saveTransaction(_transaction), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> allProducts() throws ResourceNotFoundException {

        //retrieve all customers
        List<Product> products = (List<Product>) productService.findAll();
        if(products.isEmpty()){
            throw new ResourceNotFoundException("No product found.");
        }

        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    //update product
    @PutMapping("/{product_id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable("product_id") Integer product_id,
            @Valid @RequestBody Product product) throws ResourceNotFoundException{

        Product checkProduct = productService.findById(product_id).map((_product) -> {
            _product.setName(product.getName());
            _product.setDescription(product.getDescription());

            return productService.save(_product);
        }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found."));

        return new ResponseEntity<>(checkProduct, HttpStatus.OK);
    }

    @GetMapping("getinfo/{id}")    //path variable
    public ResponseEntity<Object> getProductById(@PathVariable("id") Integer id) throws ResourceNotFoundException {

        // 1. Find the USER
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByEmail(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        //Get the customer by Id and return the response
        Product product = productService.findById(id).orElseThrow(()->new ResourceNotFoundException("Customer not found."));

        return new ResponseEntity<>(product, HttpStatus.OK); //200

    }

    @DeleteMapping("/{id}")    //path variable
    public ResponseEntity<Object> deleteProductById(@PathVariable("id") Integer id) throws ResourceNotFoundException {

        //Get the customer by Id and return the response
        Product product = productService.findById(id).orElseThrow(()->new ResourceNotFoundException("Unable to perform the task."));
        productService.deleteById(id);

        return new ResponseEntity<>(product, HttpStatus.OK); //200

    }

    //COUNT transaction
    @GetMapping("/count")
    public ResponseEntity<Object> countProduct() {
        return new ResponseEntity<>(productService.count(), HttpStatus.OK);
    }
}
