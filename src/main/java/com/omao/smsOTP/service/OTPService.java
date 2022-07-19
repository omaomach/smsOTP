package com.omao.smsOTP.service;

import com.omao.smsOTP.config.TwilioConfig;
import com.omao.smsOTP.dto.OtpStatus;
import com.omao.smsOTP.dto.PasswordResetRequestDto;
import com.omao.smsOTP.dto.PasswordResetResponseDto;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {

    @Autowired
    private TwilioConfig twilioConfig;

    Map<String, String> otpMap = new HashMap<>();

    public Mono<PasswordResetResponseDto> sendOTPForPasswordReset(PasswordResetRequestDto passwordResetRequestDto) {

        PasswordResetResponseDto passwordResetResponseDto = null;

        try {

            PhoneNumber to = new PhoneNumber(passwordResetRequestDto.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            String otp = generateOTP();
            String otpMessage = "Dear user, your OTP is ##" + otp + "##. Use this Passcode to complete your password reset. Thank you";

            Message message = Message
                    .creator(to, from, otpMessage)
                    .create();

            otpMap.put(passwordResetRequestDto.getUserName(), otp); // not recommended, an in memory database should be used

            passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.DELIVERED, otpMessage);

        }catch (Exception exception) {
            passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.FAILED, exception.getMessage());
        }

        return Mono.just(passwordResetResponseDto);

    }

    public Mono<String> validateOTP(String userInputOTP, String username) {

        if (userInputOTP.equals(otpMap.get(username))) {
            otpMap.remove(username, userInputOTP);
            return Mono.just("Valid OTP, please proceed");
        }else {
            return Mono.error(new IllegalArgumentException("Invalid OTP, please try again"));
        }

    }

    //6 digit OTP
    private String generateOTP() {

        return new DecimalFormat("000000")
                .format(new Random().nextInt(99999));

    }

}
