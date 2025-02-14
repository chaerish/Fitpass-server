package com.example.fitpassserver.domain.fitness.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberFitnessCustomRepositoryImpl implements MemberFitnessCustomRepository {

    private final EntityManager entityManager;

    @Override
    public void createEventSchedulerUpdateStatusIsProgress(Long memberFitnessId) {
        String eventName = "update_member_fitness_status_as_done_" + memberFitnessId;
        String sql = "CREATE EVENT " + eventName + " " +
                        "ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR " +
                        "ON COMPLETION NOT PRESERVE " +
                        "DO UPDATE member_fitness SET status = 'DONE' " +
                        "WHERE id = " + memberFitnessId;

        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
