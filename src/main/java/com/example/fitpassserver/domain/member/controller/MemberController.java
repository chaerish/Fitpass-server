package com.example.fitpassserver.domain.member.controller;


import com.example.fitpassserver.domain.member.converter.MemberConverter;
import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.service.command.MemberCommandService;
import com.example.fitpassserver.domain.member.validation.validator.CheckLoginIdValidator;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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

}
