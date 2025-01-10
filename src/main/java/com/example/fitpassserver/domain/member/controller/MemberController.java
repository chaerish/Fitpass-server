package com.example.fitpassserver.domain.member.controller;


import com.example.fitpassserver.domain.member.converter.MemberConverter;
import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.service.command.MemberCommandService;
import com.example.fitpassserver.domain.member.validation.validator.CheckLoginIdValidator;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final CheckLoginIdValidator checkLoginIdValidator; //중복 아이디 체크

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkLoginIdValidator);
    }

    @Operation(summary = "회원가입 api", description = "회원가입을 위한 api입니다.")
    @PostMapping("/register")
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join(@RequestBody @Valid MemberRequestDTO.JoinDTO request){
        Member member = memberCommandService.joinMember(request);
        return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }
    @Operation(summary = "아이디 중복 확인 api", description = "중복 아이디 확인을 위한 api입니다.")
    @GetMapping("/checkLoginId")
    public ApiResponse<?> checkLoginId(@RequestBody @Valid MemberRequestDTO.CheckLoginId request){
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "로그인 api", description = "로그인을 위한 api입니다.")
    @Parameters({
            @Parameter(name = "dto", description = "로그인을 위한 아이디와 비밀번호 입력 DTO")
    })
    @PostMapping("/login")
    public ApiResponse<MemberResponseDTO.MemberTokenDTO> login(@RequestBody MemberRequestDTO.LoginDTO dto) {
        return ApiResponse.onSuccess(memberCommandService.login(dto));
    }

    @Operation(summary = "리프레시 토큰 갱신 api", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰 발급하는 api입니다..")
    @Parameters({
            @Parameter(name = "Refresh-Token", description = "리프레시 토큰")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.",
                    content = @Content(schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다.",
                    content = @Content(schema = @Schema(implementation = io.swagger.v3.oas.annotations.responses.ApiResponse.class)))
    })
    @PostMapping("/refresh")
    public ApiResponse<MemberResponseDTO.MemberTokenDTO> refreshToken(
            @RequestHeader(value = "Refresh-Token", required = true) String refreshToken) {

        // Refresh Token이 비어 있거나 잘못된 경우 처리
        if (refreshToken == null || refreshToken.isBlank()) {
            log.error("Refresh token is missing or invalid.");
            throw new MemberException(MemberErrorCode.INVALID_TOKEN);
        }

        // 토큰 재발급
        MemberResponseDTO.MemberTokenDTO tokenResponse = memberCommandService.refreshToken(refreshToken);
        return ApiResponse.onSuccess(tokenResponse);
    }


}
