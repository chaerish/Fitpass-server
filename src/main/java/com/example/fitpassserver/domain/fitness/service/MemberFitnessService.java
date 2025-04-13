package com.example.fitpassserver.domain.fitness.service;


import com.example.fitpassserver.domain.fitness.converter.MemberFitnessConverter;
import com.example.fitpassserver.domain.fitness.dto.response.MemberFitnessResDTO;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.domain.review.service.ReviewReminderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberFitnessService {
    private final MemberFitnessRepository memberFitnessRepository;
     private final MemberRepository memberRepository;
     private final FitnessImageService fitnessImageService;
     private final ReviewReminderService reviewReminderService;

     private void checkMember(Member member, MemberFitness memberFitness){
         if(!memberFitness.getMember().getId().equals(member.getId())){
             throw new FitnessException(FitnessErrorCode.USER_MISMATCH);
         }
     }

    public MemberFitnessResDTO.MemberFitnessGroupDTO getPassList(String id) {
        // 유저 찾기
        Member member = memberRepository.findByLoginId(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // 해당 유저의 패스 찾기
        List<MemberFitness> memberFitnessList = memberFitnessRepository.findAllByMember(member);

        // 모든 Fitness ID에 대한 이미지 미리 조회
        Map<Long, String> imageUrlMap = memberFitnessList.stream()
                .map(MemberFitness::getFitness)
                .map(Fitness::getId)
                .distinct()
                .collect(Collectors.toMap(
                        fitnessId -> fitnessId,
                        fitnessImageService::getFitnessImage
                ));

        return MemberFitnessConverter.toGroupDto(memberFitnessList, imageUrlMap);
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
        memberFitnessRepository.createEventSchedulerUpdateStatusIsProgress(memberFitness.getId());

        reviewReminderService.reserve(memberFitness);
    }

    public MemberFitnessResDTO.MemberFitnessPreviewDTO getPass(Member member, Long passId) {
        MemberFitness memberFitness = memberFitnessRepository.findById(passId)
                .orElseThrow(() -> new FitnessException(FitnessErrorCode.PASS_NOT_FOUND));

        checkMember(member, memberFitness);

        // 이미지 URL 조회
        String imageUrl = fitnessImageService.getFitnessImage(memberFitness.getFitness().getId());

        return MemberFitnessConverter.toDto(memberFitness, imageUrl);
    }

}
