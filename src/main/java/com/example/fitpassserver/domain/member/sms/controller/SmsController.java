package com.example.fitpassserver.domain.member.sms.controller;

import com.example.fitpassserver.domain.member.sms.dto.SmsRequestDTO;
import com.example.fitpassserver.domain.member.sms.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SmsController {

    private final SmsService smsService;

    public SmsController(@Autowired SmsService smsService){
        this.smsService = smsService;
    }
    @Operation(summary = "전화번호 인증번호 발송 api", description = "인증번호 발송을 위한 api입니다.")
    @PostMapping("/phoneNumberCheck")
    public ResponseEntity<?> SendSMS(@RequestBody @Valid SmsRequestDTO.CodeSendDTO smsRequestDto){
        smsService.SendSms(smsRequestDto);
        return ResponseEntity.ok("문자를 전송했습니다.");
    }
    @Operation(summary = "전화번호 인증번호 검증 api", description = "인증번호 검증을 위한 api입니다.")
    @PostMapping("/verification")
    public ResponseEntity<?> verifyCode(@RequestBody @Valid SmsRequestDTO.CodeVertifyDTO smsVerifyDto){
        boolean verify = smsService.verifyCode(smsVerifyDto);
        if (verify) {
            return ResponseEntity.ok("확인 되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증에 실패했습니다.");
        }
    }
}
