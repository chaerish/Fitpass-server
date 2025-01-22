package com.example.fitpassserver.domain.fitness.util;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PassStatusScheduler {

    private final MemberFitnessRepository memberFitnessRepository;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void updatePassStatus() {
        List<MemberFitness> memberFitnessList = memberFitnessRepository.findByStatusAndActiveTimeBefore(Status.PROGRESS, LocalDateTime.now().minusHours(1));
        memberFitnessList.forEach(MemberFitness::done);
        memberFitnessRepository.saveAll(memberFitnessList);
    }
}