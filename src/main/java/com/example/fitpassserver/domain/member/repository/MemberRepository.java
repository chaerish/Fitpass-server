package com.example.fitpassserver.domain.member.repository;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.MemberStatus;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByName(String name);

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

    Optional<Member> findByPhoneNumberAndStatusIs(String phoneNumber, MemberStatus status);

    Optional<Member> findByIdAndStatusIs(Long id, MemberStatus status);
}
