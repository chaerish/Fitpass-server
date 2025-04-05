package com.example.fitpassserver.global.common.service;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.global.common.dto.CommonRequestDTO;
import com.example.fitpassserver.global.common.dto.CommonResponseDTO;

public interface CommonService {
    CommonResponseDTO.MemberTokenDTO login(CommonRequestDTO.LoginDTO dto);

    boolean checkLoginId(String loginId);

    CommonResponseDTO.MemberTokenDTO refreshToken(String refreshToken);

    String getLoginId(MemberRequestDTO.FindLoginIdDTO requst);

    String findPassword(MemberRequestDTO.FindPasswordDTO requst);

    void resetPassword(MemberRequestDTO.ResetPasswordDTO request);


}
