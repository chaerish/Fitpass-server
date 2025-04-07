package com.example.fitpassserver.global.common.controller;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.validation.validator.CheckLoginIdValidator;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.global.common.dto.CommonRequestDTO;
import com.example.fitpassserver.global.common.dto.CommonResponseDTO;
import com.example.fitpassserver.global.common.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "모든 회원(사업자, 일반) API", description = "사업자, 일반 회원 관련 API입니다.")
public class CommonController {

    private final CheckLoginIdValidator checkLoginIdValidator;
    private final CommonService commonService;

    @Operation(summary = "로그인 api", description = "로그인을 위한 api입니다.")
    @PostMapping("/login")
    public ApiResponse<CommonResponseDTO.MemberTokenDTO> login(@RequestBody CommonRequestDTO.LoginDTO dto) {
        return ApiResponse.onSuccess(commonService.login(dto));
    }

    @Operation(summary = "리프레시 토큰 갱신 api", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰 발급하는 api입니다.")
    @Parameters({
            @Parameter(name = "Refresh-Token", description = "리프레시 토큰")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.",
                    content = @Content(schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class)))
    })
    @PostMapping("/refresh")
    public ApiResponse<CommonResponseDTO.MemberTokenDTO> refreshToken(
            @RequestHeader(value = "Refresh-Token", required = true) String refreshToken) {

        // Refresh Token이 비어 있거나 잘못된 경우 처리
        if (refreshToken == null || refreshToken.isBlank()) {
            log.error("Refresh token is missing or invalid.");
            throw new MemberException(MemberErrorCode.INVALID_TOKEN);
        }

        // 토큰 재발급
        CommonResponseDTO.MemberTokenDTO tokenResponse = commonService.refreshToken(refreshToken);
        return ApiResponse.onSuccess(tokenResponse);
    }

    @Operation(summary = "아이디 중복 확인 api", description = "중복 아이디 확인을 위한 api입니다.")
    @GetMapping("/check/login-id")
    public ApiResponse<?> checkLoginId(@RequestParam("loginId") String loginId) {
        boolean isDuplicate = commonService.checkLoginId(loginId);
        return ApiResponse.onSuccess(isDuplicate);
    }

    @Operation(summary = "아이디 찾기 api", description = "아이디 찾기 api입니다.")
    @PostMapping("/find-id")
    public ApiResponse<?> findId(@RequestBody @Valid MemberRequestDTO.FindLoginIdDTO request) {
        String loginId = commonService.getLoginId(request);
        return ApiResponse.onSuccess(loginId);
    }

    @Operation(summary = "비밀번호 찾기 api", description = "비밀번호 찾기 api입니다.")
    @PostMapping("/find-password")
    public ApiResponse<?> findPassword(@RequestBody @Valid MemberRequestDTO.FindPasswordDTO request) {
        return ApiResponse.onSuccess(commonService.findPassword(request));
    }

    @Operation(summary = "비밀번호 리셋 api", description = "비밀번호 찾기 후 비밀번호를 리셋하는 api입니다.")
    @PatchMapping("/reset-password")
    public ApiResponse<?> resetPassword(@RequestBody @Valid MemberRequestDTO.ResetPasswordDTO request) {
        commonService.resetPassword(request);
        return ApiResponse.onSuccess("비밀번호 변경 완료");
    }
}
