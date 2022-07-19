package com.omao.smsOTP.resource;

import com.omao.smsOTP.dto.PasswordResetRequestDto;
import com.omao.smsOTP.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TwilioOTPHandler {

    @Autowired
    private OTPService otpService;

    public Mono<ServerResponse> sendOTP(ServerRequest serverRequest) {
        
        return serverRequest.bodyToMono(PasswordResetRequestDto.class)
                .flatMap(dto-> otpService.sendOTPForPasswordReset(dto))
                .flatMap(dto->ServerResponse.status(HttpStatus.OK)
                        .body(BodyInserters.fromValue(dto)));

    }

    public Mono<ServerResponse> validateOTP(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(PasswordResetRequestDto.class)
                .flatMap(dto-> otpService.validateOTP(dto.getOtp(), dto.getUserName()))
                .flatMap(dto->ServerResponse.status(HttpStatus.OK)
                        .bodyValue(dto));

    }

}
