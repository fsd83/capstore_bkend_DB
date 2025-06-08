package com.tck.capBackend.Service;

import com.tck.capBackend.DTO.RequestResponse;
import com.tck.capBackend.Exception.PasswordBlankException;
import com.tck.capBackend.Repository.CustomerRepository;
import com.tck.capBackend.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RequestResponse signUp(RequestResponse registrationRequest) throws Exception, PasswordBlankException {
        RequestResponse requestResponse = new RequestResponse();

        Customer customer = new Customer();

        customer.setFirstName(registrationRequest.getFirstName());
        customer.setLastName(registrationRequest.getLastName());
        customer.setEmail(registrationRequest.getEmail());
        customer.setPhone(registrationRequest.getPhone());

        if(registrationRequest.getPassword().isBlank()) {
            throw new PasswordBlankException();
        }

        customer.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        customer.setRole((registrationRequest.getRole()));

        Customer result = customerRepository.save(customer);

        if(result != null && result.getId() > 0) {
            //TODO - return response of customer
            //requestResponse.setCustomer(customer);    Sensitive info included - undesirable
            requestResponse.setFirstName(customer.getFirstName());
            requestResponse.setLastName(customer.getLastName());
            requestResponse.setEmail(customer.getEmail());
            requestResponse.setMessage("Customer saved successfully.");
        }
        return requestResponse;
    }

    public RequestResponse signIn(RequestResponse signinRequest) {

        RequestResponse requestResponse = new RequestResponse();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

        var customer = customerRepository.findByEmail(signinRequest.getEmail()).orElseThrow();

        var jwt = jwtUtils.generateToken(customer);

        var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), customer);

        requestResponse.setToken(jwt);
        requestResponse.setRefreshToken(refreshToken);
        requestResponse.setExpirationTime("24Hr");
        requestResponse.setMessage("Signed in successfully");

        return requestResponse;
    }

    public RequestResponse refreshToken(RequestResponse refreshTokenRequest) {

        RequestResponse requestResponse = new RequestResponse();

        String customerEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());

        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow();

        if(jwtUtils.isTokenValid(refreshTokenRequest.getToken(), customer)) {
            var jwt = jwtUtils.generateToken(customer);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), customer);

            requestResponse.setToken(jwt);
            requestResponse.setRefreshToken(refreshToken);
            requestResponse.setExpirationTime("24Hr");
            requestResponse.setMessage("Refreshed token successfully");
        }

        return requestResponse;
    }

    // returns the user information
    public RequestResponse profile(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //System.out.println(authentication); //prints the details of the user(name,email,password,roles e.t.c)
        //System.out.println(authentication.getDetails()); // prints the remote ip
        //System.out.println(authentication.getName()); //prints the EMAIL, the email was stored as the unique identifier

        var customer = new Customer();
        customer = customerRepository.findByEmail(authentication.getName()).orElseThrow();

        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setFirstName(customer.getFirstName());
        requestResponse.setLastName(customer.getLastName());
        requestResponse.setEmail(customer.getEmail());
        requestResponse.setPassword(customer.getPassword());

        return requestResponse;

    }

    // update the user information
    public RequestResponse updateProfile(RequestResponse updateRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //System.out.println(authentication); //prints the details of the user(name,email,password,roles e.t.c)
        //System.out.println(authentication.getDetails()); // prints the remote ip
        //System.out.println(authentication.getName()); //prints the EMAIL, the email was stored as the unique identifier

        var customer = new Customer();
        customer = customerRepository.findByEmail(authentication.getName()).map(_customer->{
            _customer.setFirstName((updateRequest.getFirstName()));
            _customer.setLastName((updateRequest.getLastName()));
            _customer.setEmail((updateRequest.getEmail()));
            _customer.setPhone((updateRequest.getPhone()));
            _customer.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
            return customerRepository.save(_customer);
                })
                .orElseThrow();

        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setMessage("Profile update successfully");
        requestResponse.setFirstName(customer.getFirstName());
        requestResponse.setLastName(customer.getLastName());
        requestResponse.setEmail(customer.getEmail());
        requestResponse.setPhone(customer.getPhone());
        requestResponse.setPassword(customer.getPassword());

        return requestResponse;

    }



}
