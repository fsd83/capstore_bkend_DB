package com.tck.capBackend.Controller;

import com.tck.capBackend.Exception.ResourceNotFoundException;
import com.tck.capBackend.Service.ProductService;
import com.tck.capBackend.models.Customer;
import com.tck.capBackend.models.Product;
import com.tck.capBackend.models.Transaction;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    ProductService productService;

    //YAN - edited to add the product, using requestpart
    @PostMapping("/add")
    public ResponseEntity<Object> addProduct(@Valid @RequestPart Product product, @RequestPart MultipartFile imageFile) throws Exception{

        //to remove try-catch block
        try{
            //savedProduct
            System.out.println(product);
            Product savedProduct = productService.addProduct(product, imageFile);
            // save the product to the database
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //YAN - TODO - to edit the async and await + fetch part in VS file - listing first. This part is after fetching
    // to use product_id or id??
    @GetMapping("/{product_id}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable("product_id") Integer product_id){
        Product product = productService.getProductById(product_id); // or can use findById?
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImagePath()))
                .body(imageFile);
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

    //YAN - Asking if the param id is which? product_ id or transaction_id?
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
