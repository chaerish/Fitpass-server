package com.example.fitpassserver.domain.member.sms.service;

import com.example.fitpassserver.domain.member.sms.dto.SmsRequestDTO;

public interface SmsService {
    void SendSms(SmsRequestDTO.CodeSendDTO smsRequestDto);
    boolean verifyCode(SmsRequestDTO.CodeVertifyDTO smsVerifyDto);
}
