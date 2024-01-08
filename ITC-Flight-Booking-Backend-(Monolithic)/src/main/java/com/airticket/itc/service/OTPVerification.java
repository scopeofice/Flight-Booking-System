package com.airticket.itc.service;

import com.airticket.itc.dto.VerifiactionResponseDTO;

public interface OTPVerification {
    boolean matchOTP(VerifiactionResponseDTO otp);
    void sendOTP(String email);
}
