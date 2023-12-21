package com.airticket.itc.controller;

import com.airticket.itc.dto.VerifiactionResponseDTO;
import com.airticket.itc.dto.VerificationRequestDTO;
import com.airticket.itc.service.OTPVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailVerificationController {
    @Autowired
    OTPVerification otpVerification;

    @PostMapping("/otp-request")
    public ResponseEntity<String> OTPRequest(@RequestBody VerificationRequestDTO request){
        otpVerification.sendOTP(request.getEmail());
        return new ResponseEntity<>("OTP sent successfully", HttpStatus.OK);
    }
    @PostMapping("/otp-verification")
    public ResponseEntity<String> OTPVerification(@RequestBody VerifiactionResponseDTO otp) {
        boolean success = otpVerification.matchOTP(otp);

        if (success) {
            return new ResponseEntity<>("OTP Matched!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("OTP did not Matched!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
