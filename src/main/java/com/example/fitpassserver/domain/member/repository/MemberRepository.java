package com.example.fitpassserver.domain.member.repository;

import com.example.fitpassserver.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);

    //활성화된 멤버중에서만 중복 번호 찾도록
    @Query("SELECT COUNT(m) > 0 FROM Member m WHERE m.phoneNumber = :phoneNumber AND m.isDeleted = false")
    boolean existsActiveByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);

    boolean existsByLoginId(String loginId);

    Optional<Member> findByProviderId(String providerId);
}
