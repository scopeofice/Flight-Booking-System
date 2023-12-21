package com.airticket.itc.service;

import com.airticket.itc.dto.EmailDTO;
import com.airticket.itc.dto.VerifiactionResponseDTO;
import com.airticket.itc.repo.OTPrepo;
import com.airticket.itc.entity.OTP;
import com.airticket.itc.util.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OTPVerificationImpl implements OTPVerification{
    @Autowired
    EmailService emailService;
    @Autowired
    OTPrepo otpRepo;
    @Override
    public boolean matchOTP(VerifiactionResponseDTO otp) {
        if(otpRepo.existsByEmail(otp.getEmail())){
            if(otp.getOtp().equals(otpRepo.findByEmail(otp.getEmail()).get().getOtp())){
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public void sendOTP(String email) {

            String newOtp = AccountUtils.generateOTP();

            if(otpRepo.existsByEmail(email)){
                OTP existingOTP = otpRepo.findByEmail(email).get();
                existingOTP.setOtp(newOtp);
                otpRepo.save(existingOTP);
            }else {
            OTP otp = OTP.builder()
                    .email(email)
                    .otp(newOtp)
                    .build();
                OTP savedOTP = otpRepo.save(otp);
            }

            EmailDTO emailDetails = EmailDTO.builder()
                    .recipient(email)
                    .subject("Confirm Email")
                    .messageBody("Your OTP : "+newOtp )
                    .build();
            emailService.sendEmailAlert(emailDetails);


    }

}
