package com.tck.capBackend.Controller;

import com.tck.capBackend.DTO.RequestResponse;
import com.tck.capBackend.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restricted")
@CrossOrigin("*")
public class RestrictedController {
    //allow the user to view his/her profile
    @Autowired
    private AuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<RequestResponse> getProfile() {
        return new ResponseEntity<>(authService.profile(), HttpStatus.OK);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<RequestResponse> updateProfile(@Valid @RequestBody RequestResponse updateProfileRequest) {
        return new ResponseEntity<>(authService.updateProfile(updateProfileRequest), HttpStatus.OK);
    }


}
