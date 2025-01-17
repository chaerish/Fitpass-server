package com.example.fitpassserver.domain.profile.repositroy;

import com.example.fitpassserver.domain.profile.entity.Profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findProfileByMemberId(Long memberId);
}
