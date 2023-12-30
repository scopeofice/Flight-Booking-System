package com.airticket.itc.service;

import com.airticket.itc.dto.AuthResponse;
import com.airticket.itc.dto.LoginDTO;
import com.airticket.itc.dto.UserRequestDTO;


public interface UserService {
    AuthResponse createAccount(UserRequestDTO userRequest);
    AuthResponse login(LoginDTO request);
    UserRequestDTO editUser(UserRequestDTO request);
    String resetPassword(LoginDTO request);
    String deleteUser (LoginDTO user);
}
