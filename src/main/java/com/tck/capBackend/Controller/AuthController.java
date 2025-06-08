package com.tck.capBackend.Controller;

import com.tck.capBackend.DTO.RequestResponse;
import com.tck.capBackend.Exception.PasswordBlankException;
import com.tck.capBackend.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    //register an account
    @PostMapping("/signup")
    public ResponseEntity<RequestResponse> signup(@Valid @RequestBody RequestResponse signUpRequest) throws PasswordBlankException, Exception {
        return new ResponseEntity<>(authService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    //signin to an account
    @PostMapping("/signin")
    public ResponseEntity<RequestResponse> signIn(@RequestBody RequestResponse signInRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authService.signIn(signInRequest));
    }

    //refresh token for an account
    @PostMapping("/refresh")
    public ResponseEntity<RequestResponse> refreshToken(@RequestBody RequestResponse tokenRequest){
        return new ResponseEntity<>(authService.refreshToken(tokenRequest), HttpStatus.OK);
    }
}
