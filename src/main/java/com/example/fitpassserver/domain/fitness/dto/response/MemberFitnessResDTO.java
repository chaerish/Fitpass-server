package com.example.fitpassserver.domain.fitness.dto.response;

import com.example.fitpassserver.domain.fitness.entity.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public class MemberFitnessResDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberFitnessPreviewDTO{
        private Long id;
        private Status status; // NONE, PROGRESS, DONE
        private LocalDateTime activeTime;
        private boolean isAgree;
        private Long memberId;
        private Long fitnessId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberFitnessGroupDTO {
        private List<MemberFitnessPreviewDTO> none;
        private List<MemberFitnessPreviewDTO> progress;
        private List<MemberFitnessPreviewDTO> done;
    }
}
