package com.example.fitpassserver.domain.member.entity;

import com.example.fitpassserver.domain.CoinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.global.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE member SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

//    @Column(name = "email")
//    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "provider",nullable = true)
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "is_agree", nullable = false)
    private boolean isAgree;

    @Column(name = "is_terms_agreed", nullable = false)
    private boolean isTermsAgreed;

    @Column(name = "is_location_agreed", nullable = false)
    private boolean isLocationAgreed;

    @Column(name = "is_third_party_agreed", nullable = false)
    private boolean isThirdPartyAgreed;

    @Column(name = "is_marketing_agreed",nullable = false)
    private Boolean isMarketingAgreed;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)")
    public MemberStatus status;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Coin> CoinList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<CoinPaymentHistory> CoinPaymentHistoryList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberFitness> MemberFitnessList = new ArrayList<>();

    public void encodePassword(String password) {
        this.password = password;
    }
    //status 변경
    public void deactivateAccount() {
        if (this.status == MemberStatus.INACTIVE) {
            throw new MemberException(MemberErrorCode.ALREADY_DELETED);
        }
        this.status = MemberStatus.INACTIVE;
    }

}
