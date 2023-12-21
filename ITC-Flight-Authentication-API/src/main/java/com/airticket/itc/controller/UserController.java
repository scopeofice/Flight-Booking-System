package com.airticket.itc.controller;

import com.airticket.itc.dto.*;
import com.airticket.itc.exception.UserNotFoundException;
import com.airticket.itc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public AuthResponse createAccount(@RequestBody UserRequestDTO userRequest){
        return userService.createAccount(userRequest);
    }

    @PostMapping("/login")
    public AuthResponse userLogin(@RequestBody LoginDTO request){
        return userService.login(request);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> closeAccount(@RequestBody LoginDTO request) {
        try {
            String resp = userService.deleteUser(request);

            if ("Account deleted Successfully".equals(resp)) {
                return ResponseEntity.ok(resp);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
