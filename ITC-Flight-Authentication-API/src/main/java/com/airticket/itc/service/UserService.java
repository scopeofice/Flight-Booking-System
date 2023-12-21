package com.airticket.itc.service;

import com.airticket.itc.dto.*;


public interface UserService {
    AuthResponse createAccount(UserRequestDTO userRequest);
    AuthResponse login(LoginDTO request);
    String deleteUser (LoginDTO user);
}
