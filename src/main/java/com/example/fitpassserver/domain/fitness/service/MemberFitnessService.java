package com.example.fitpassserver.domain.fitness.service;


import com.example.fitpassserver.domain.fitness.converter.MemberFitnessConverter;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.fitness.dto.response.MemberFitnessResDTO;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberFitnessService {
    private final MemberFitnessRepository memberFitnessRepository;
    // MemberRepository 의존성 추가
     private final MemberRepository memberRepository;

     private void checkMember(Member member, MemberFitness memberFitness){
         if(!memberFitness.getMember().getId().equals(member.getId())){
             throw new FitnessException(FitnessErrorCode.USER_MISMATCH);
         }
     }

    public MemberFitnessResDTO.MemberFitnessGroupDTO getPassList(String id){
        // 유저 찾기
        Member member = memberRepository.findByLoginId(id).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        // 해당 유저의 패스 찾기 -> 리포지토리 메서드 추가
        List<MemberFitness> memberFitnessList = memberFitnessRepository.findAllByMember(member);
        return MemberFitnessConverter.toGroupDto(memberFitnessList);
    }

    public void usePass(Member member, Long passId, boolean isAgree){
        if(!isAgree){
            throw new FitnessException(FitnessErrorCode.AGREEMENT_NOT_CHECKED);
        }
        // 로그인한 유저와 패스의 유저가 다를 경우도 예외처리
        // 해도되고 안해도됨
        MemberFitness memberFitness = memberFitnessRepository.findById(passId).orElseThrow(() -> new FitnessException(FitnessErrorCode.UNAVAILABLE_PASS));

        checkMember(member, memberFitness);

        if(!memberFitness.getStatus().equals(Status.NONE)){
            throw new FitnessException(FitnessErrorCode.INVALID_PASS);
        }

        memberFitness.use();
    }

    public MemberFitnessResDTO.MemberFitnessPreviewDTO getPass(Member member, Long passId){
        MemberFitness memberFitness = memberFitnessRepository.findById(passId).orElseThrow(() -> new FitnessException(FitnessErrorCode.PASS_NOT_FOUND));

        checkMember(member, memberFitness);

        return MemberFitnessConverter.toDto(memberFitness);
    }
}
