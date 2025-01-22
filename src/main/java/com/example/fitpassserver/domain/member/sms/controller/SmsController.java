package com.example.fitpassserver.domain.member.sms.controller;

import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.sms.dto.SmsRequestDTO;
import com.example.fitpassserver.domain.member.sms.service.SmsService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SmsController {

    private final SmsService smsService;

    public SmsController(@Autowired SmsService smsService) {
        this.smsService = smsService;
    }

    @Operation(summary = "전화번호 인증번호 발송 api", description = "인증번호 발송을 위한 api입니다.")
    @PostMapping("/verify-code")
    public ApiResponse<?> SendSMS(@RequestBody @Valid SmsRequestDTO.CodeSendDTO smsRequestDto) {
        smsService.SendSms(smsRequestDto);
        return ApiResponse.onSuccess("문자를 전송했습니다.");
    }

    @Operation(summary = "전화번호 인증번호 검증 api", description = "인증번호 검증을 위한 api입니다.")
    @PostMapping("/verification")
    public ApiResponse<?> verifyCode(@RequestBody @Valid SmsRequestDTO.CodeVertifyDTO smsVerifyDto) {
        boolean verify = smsService.verifyCode(smsVerifyDto);
        if (verify) {
            return ApiResponse.onSuccess("인증이 완료되었습니다.");
        } else {
            throw new MemberException(MemberErrorCode.INCORRECT_CODE);
        }
    }
}
