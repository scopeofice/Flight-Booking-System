package com.airticket.itc.service;


import com.airticket.itc.dto.EmailDTO;

public interface EmailService {
    void sendEmailAlert(EmailDTO emailDetails);
}
