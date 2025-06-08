package com.tck.capBackend.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tck.capBackend.Repository.CustomerRepository;
import com.tck.capBackend.Service.CustomerService;
import com.tck.capBackend.models.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)   //disable security filters while testing
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "";

    private Customer customer1, customer2;
    private final List<Customer> customerList = new ArrayList<>();

    //run setup b4 each unit test is performed
    @BeforeEach
    void setUp() {
        //delete all customer records b4 each test
        customerRepository.deleteAll();

        customer1 = new Customer("John", "Doe", "johndoe@gmail.com");
        customer2 = new Customer("Sam", "Smith", "samsmith@yahoo.com");

        customerList.add(customer1);
        customerList.add(customer2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("JUNIT test: Add a customer")
    void addCustomer() throws Exception {
        //arrange - prepare
        String requestBody = objectMapper.writeValueAsString(customer1);

        //act - action or behaviour
        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT.concat("/add"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the output
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(customer1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer1.getLastName()))
                .andExpect(jsonPath("$.email").value(customer1.getEmail()));

    }

    @Test
    @DisplayName("JUNIT test: Get all customers")
    void allCustomers() throws Exception {
        //arrange
        customerRepository.saveAll(customerList);

        //act - action or behaviour
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/all")));

        //assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(customerList.size())));

    }

    @Test
    @DisplayName("JUNIT test: Update a customer")
    void updateCustomers() throws Exception{
        //arrange - prepare
        customerService.save(customer1);

        Customer customer = customerService.findById(customer1.getId()).get();

        customer.setFirstName("jim_updated");
        customer.setLastName("doe_updated");
        customer.setEmail("jimdoe_updated@gmail.com");

        String requestBody = objectMapper.writeValueAsString(customer);


        //act - action or behaviour
        ResultActions resultActions = mockMvc.perform(put(API_ENDPOINT.concat("/{id}"), customer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(customer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer.getLastName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()));
    }

    @Test
    @DisplayName("JUNIT test: Get a customer by ID")
    void getCustomerById() throws Exception {
        //arrange - prepare
        customerService.save(customer1);

        //act - action or behaviour
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/getInfo/{id}"), customer1.getId()));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(customer1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer1.getLastName()))
                .andExpect(jsonPath("$.email").value(customer1.getEmail()));
    }

    @Test
    @DisplayName("JUNIT test: Delete a customer by ID")
    void deleteCustomerById() throws Exception{
        //arrange - prepare
        customerService.save(customer2);

        //act - action or behaviour
        ResultActions resultActions = mockMvc.perform(delete(API_ENDPOINT.concat("/{id}"), customer2.getId()));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(customer2.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer2.getLastName()))
                .andExpect(jsonPath("$.email").value(customer2.getEmail()));

    }

    @Test
    @DisplayName("JUNIT test: gET a customer by email or lastName")
    void getCustomerByEmailOrLastName() throws Exception{
        //arrange - prepare
        customerRepository.saveAll(customerList);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        requestParams.add("email", customer1.getEmail());
        requestParams.add("lastName", customer1.getLastName());

        //act - action or behaviour
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT).params(requestParams).contentType(MediaType.APPLICATION_JSON));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].firstName").value(customer1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(customer1.getLastName()))
                .andExpect(jsonPath("$[0].email").value(customer1.getEmail()));
                /* Note in this case customerController() returns a customer list */
    }
}