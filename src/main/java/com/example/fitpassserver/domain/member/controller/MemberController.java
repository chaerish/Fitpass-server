package com.example.fitpassserver.domain.member.controller;


import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.converter.MemberConverter;
import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.service.command.MemberCommandService;
import com.example.fitpassserver.domain.member.service.query.MemberQueryService;
import com.example.fitpassserver.domain.member.validation.validator.CheckLoginIdValidator;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/member")
@Tag(name = "일반 회원 API", description = "일반 회원 관련 API입니다.")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final CheckLoginIdValidator checkLoginIdValidator;

    @InitBinder("joinDTO")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkLoginIdValidator);
    }

    @Operation(summary = "회원가입 api", description = "회원가입을 위한 api입니다.")
    @PostMapping("/register")
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join(@RequestBody @Valid MemberRequestDTO.MemberJoinDTO request) {
        Member member = memberCommandService.joinMember(request);
        return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }

    @Operation(summary = "소셜 로그인 후 추가 정보 입력받아 회원가입하는 api", description = "소셜 로그인 이후 추가 정보를 입력받아 회원가입하는 api입니다.")
    @PostMapping("/oauth2/register")
    public ApiResponse<?> oAuth2Join(@RequestBody @Valid MemberRequestDTO.SocialJoinDTO request, @CookieValue(value = "accessToken") String accessToken) {
        Member updatedMember = memberCommandService.socialJoinMember(request, accessToken);
        return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(updatedMember));
    }

    @Operation(summary = "사용자 위치 설정 api", description = "위도 경도를 받아 사용자의 위치를 설정합니다.")
    @PatchMapping("/location")
    public ApiResponse<String> setLocation(@CurrentMember Member member, @RequestBody @Valid MemberRequestDTO.LocationDTO dto) {
        memberCommandService.setLocation(member.getLoginId(), dto);
        return ApiResponse.onSuccess("사용자의 위치가 변경되었습니다.");
    }

    @Operation(summary = "전화번호 변경 api", description = "인증된 전화번호를 변경하는 api입니다.")
    @PatchMapping("/change/phone-number")
    public ApiResponse<?> changePhoneNumber(@CurrentMember Member member, @RequestBody @Valid MemberRequestDTO.ChangePhoneNumberDTO request) {
        memberCommandService.changePhoneNumber(member, request);
        return ApiResponse.onSuccess("전화번호가 변경되었습니다.");
    }

    @Operation(summary = "비밀번호 변경 api", description = "비밀번호를 변경하는 api입니다.")
    @PatchMapping("/change/password")
    public ApiResponse<?> resetPassword(@CurrentMember Member member, @RequestBody @Valid MemberRequestDTO.ChangePasswordDTO request) {
        memberCommandService.changePassword(member, request);
        return ApiResponse.onSuccess("비밀번호 변경 완료");
    }

    @Operation(summary = "사용자 탈퇴 api", description = "사용자 탈퇴시 사용하는 api입니다..")
    @DeleteMapping("/withdraw")
    public ApiResponse<Void> deleteMember(@CurrentMember Member member) {
        memberCommandService.deactivateAccount(member);
        return ApiResponse.onSuccess(null);
    }

}
