package com.example.fitpassserver.owner.owner.repository;


import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.entity.OwnerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByLoginId(String loginId);

    Optional<Owner> findByNameAndPhoneNumber(String name, String phoneNumber);

    boolean existsByLoginId(String loginId);

    boolean existsByName(String name);

    boolean existsByPhoneNumber(String phoneNumber);

    Page<Owner> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);

    Page<Owner> findByCorporationContaining(String corporation, Pageable pageable);

    Page<Owner> findByOwnerStatus(OwnerStatus status, Pageable pageable);

}
