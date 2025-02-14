package com.example.fitpassserver.domain.fitness.util;

import com.example.fitpassserver.domain.fitness.dto.MemberFitnessRequestDTO;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import com.example.fitpassserver.domain.fitness.service.command.MemberFitnessCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PassStatusScheduler {

    private final MemberFitnessRepository memberFitnessRepository;
    private final MemberFitnessCommandService memberFitnessCommandService;

//    @Async
//    @Scheduled(fixedRate = 60000) // 1분마다 실행
//    public void updatePassStatus() {
//        List<MemberFitness> memberFitnessList = memberFitnessRepository.findByStatusAndActiveTimeBefore(Status.PROGRESS,
//                LocalDateTime.now().minusHours(1));
//        memberFitnessList.forEach(MemberFitness::done);
//        memberFitnessRepository.saveAll(memberFitnessList);
//    }

    @Async
    @Scheduled(fixedRate = 60000)
    public void cancelAfter24Hours() {
        List<MemberFitness> cancelList = memberFitnessRepository.findByStatusAndActiveTimeBefore(Status.NONE, LocalDateTime.now().minusHours(24));
        if (!cancelList.isEmpty()) {
            cancelList.forEach(memberFitness -> {
                MemberFitnessRequestDTO.CancelMemberFitnessRequestDTO dto = new MemberFitnessRequestDTO.CancelMemberFitnessRequestDTO(memberFitness.getId());
                memberFitnessCommandService.cancelFitness(memberFitness.getMember(), dto);
            });
        }

    }
}