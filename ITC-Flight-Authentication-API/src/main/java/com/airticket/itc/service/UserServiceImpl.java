package com.airticket.itc.service;

import com.airticket.itc.config.JwtTokenProvider;
import com.airticket.itc.dto.*;
import com.airticket.itc.entity.Role;
import com.airticket.itc.entity.User;
import com.airticket.itc.exception.UserNotFoundException;
import com.airticket.itc.repository.UserRepository;
import com.airticket.itc.util.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepo;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    EmailService emailService;

    @Override
    public AuthResponse createAccount(UserRequestDTO userRequest) {
        if(userRepo.existsByEmail((userRequest.getEmail()))){
            return AuthResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_ALREADY_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_ALREADY_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else{

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName((userRequest.getLastName()))
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .role(Role.ROLE_USER)
                .status("ACTIVE")
                .build();
        User savedUser = userRepo.save(newUser);

        EmailDTO emailDetails = EmailDTO.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations, Your account has been successfully created")
                .build();
        emailService.sendEmailAlert(emailDetails);

        return AuthResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .firstName(savedUser.getFirstName())
                        .lastName(savedUser.getLastName())
                        .email(savedUser.getEmail())
                        .phoneNumber(savedUser.getPhoneNumber())
                        .build())
                .build();
        }
    }

    @Override
    public AuthResponse login(LoginDTO loginDTO){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

//        EmailDTO loginAlert = EmailDTO.builder()
//                .subject("You're logged in!")
//                .recipient(loginDTO.getEmail())
//                .messageBody("You logged into your account")
//                .build();
//
//        emailService.sendEmailAlert((loginAlert));

        Optional<User> foundUser = userRepo.findByEmail(loginDTO.getEmail());

        return AuthResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .accountInfo(AccountInfo.builder()
                        .firstName(foundUser.get().getFirstName())
                        .lastName(foundUser.get().getLastName())
                        .email(foundUser.get().getEmail())
                        .phoneNumber(foundUser.get().getPhoneNumber())
                        .build())
                .build();

    }

    @Override
    public String deleteUser(LoginDTO user) {
        if(userRepo.existsByEmail(user.getEmail())){
            User existingUser = userRepo.findByEmail(user.getEmail()).orElseThrow(()-> new UserNotFoundException("User not found"));
            userRepo.delete(existingUser);
            return "Account deleted Successfully";
        }else{
            return "Account deletion Failed";
        }
    }

}
