package com.example.fitpassserver.domain.member.repository;

import com.example.fitpassserver.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);

    boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);

    boolean existsByLoginId(String loginId);

    Optional<Member> findByProviderId(String providerId);

    boolean existsByName(String name);

    int countAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime greaterThan, LocalDateTime lessThan);

    Page<Member> findByNameContaining(String name, Pageable pageable);

    Page<Member> findByLoginIdContaining(String loginId, Pageable pageable);

    Page<Member> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);
}
