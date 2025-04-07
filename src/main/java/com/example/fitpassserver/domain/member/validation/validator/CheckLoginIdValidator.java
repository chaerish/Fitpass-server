package com.example.fitpassserver.domain.member.validation.validator;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckLoginIdValidator extends AbstractValidator<MemberRequestDTO.MemberJoinDTO> {
    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(MemberRequestDTO.MemberJoinDTO dto, Errors errors) {
        if (memberRepository.existsByLoginId(dto.getLoginId())) {
            errors.rejectValue("loginId", "아이디 중복 오류", "이미 사용중인 아이디 입니다.");
        }
    }

}
