package com.airticket.itc.controller;


import com.airticket.itc.dto.AuthResponse;
import com.airticket.itc.dto.LoginDTO;
import com.airticket.itc.dto.UserRequestDTO;
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
    @PutMapping("/edit")
    public ResponseEntity<UserRequestDTO> editUser(@RequestBody UserRequestDTO request) {
        UserRequestDTO updatedUser = userService.editUser(request);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody LoginDTO request) {
        String result = userService.resetPassword(request);
        if ("Password changed successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
