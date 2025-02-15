package com.example.fitpassserver.domain.fitness.repository;

public interface MemberFitnessCustomRepository {
    void createEventSchedulerUpdateStatusIsProgress(Long memberFitnessId);
}
