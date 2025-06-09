package com.tck.capBackend.Controller;

import com.tck.capBackend.Exception.ResourceNotFoundException;
import com.tck.capBackend.Service.ProductService;
import com.tck.capBackend.models.Customer;
import com.tck.capBackend.models.Product;
import com.tck.capBackend.models.Transaction;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Object> addProduct(@Valid @RequestBody Product product) throws Exception{

        // save the product to the database
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);

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
    @PutMapping("/{transaction_id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable("Product_id") Integer product_id,
            @Valid @RequestBody Product product) throws ResourceNotFoundException{

        Product checkProduct = productService.findById(product_id).map((_product) -> {
            _product.setName(product.getName());
            _product.setDescription(product.getDescription());

            return productService.save(_product);
        }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found."));

        return new ResponseEntity<>(checkProduct, HttpStatus.OK);
    }

    @GetMapping("getInfo/{id}")    //path variable
    public ResponseEntity<Object> getProductById(@PathVariable("id") Integer id) throws ResourceNotFoundException {

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
