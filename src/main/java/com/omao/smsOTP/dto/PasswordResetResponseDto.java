package com.omao.smsOTP.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetResponseDto { // we will get the otp and status i.e whether it has been received or not

    private OtpStatus otpStatus;
    private String message;

}
