package com.airticket.itc.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCancelDTO {
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String bookingID;

}
