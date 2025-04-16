package com.example.fitpassserver.owner.owner.entity;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.member.entity.MemberStatus;
import com.example.fitpassserver.domain.member.entity.Role;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.global.common.support.LoginUser;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE owner SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("status = 'ACTIVE'")
@Table(name = "owner")
public class Owner extends BaseEntity implements LoginUser {
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

    @Column(name = "corporation", nullable = false)
    private String corporation;

    @Column(name = "business_registration_number", nullable = false) //사업자 등록번호
    private String businessRegistrationNumber;

    @Column(name = "bank_name", nullable = false) //은행명
    private String bankName;

    @Column(name = "deposit_account_name", nullable = false) //예금주명
    private String depositAccountName;

    @Column(name = "deposit_account_number", nullable = false) //사업자 계좌번호
    private String depositAccountNumber;

    @Column(name = "business_registration_url", nullable = false)
    private String businessRegistrationUrl;

    @Column(name = "bank_copy_url", nullable = false)
    private String bankCopyUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.OWNER;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)")
    @Builder.Default
    public OwnerStatus status = OwnerStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)")
    @Builder.Default
    public MemberStatus accountStatus = MemberStatus.ACTIVE;

    @Column(name = "is_agree", nullable = false)
    private boolean isAgree;

    @Column(name = "is_terms_agreed", nullable = false)
    private boolean isTermsAgreed;

    @Column(name = "is_third_party_agreed", nullable = false)
    private boolean isThirdPartyAgreed;

    @Column(name = "is_marketing_agreed", nullable = false)
    private boolean isMarketingAgreed;

    @Column(name = "is_personal_information_agreed", nullable = false)
    private boolean isPersonalInformaionAgreed;

    @Column(name = "is_additional_info")
    private boolean isAdditionalInfo;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Builder.Default
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Fitness> fitnessList = new ArrayList<>();

    public void encodePassword(String password) {
        this.password = password;
    }

    //accountStatus 변경(계정 탈퇴)
    public void deactivateAccount() {
        if (this.accountStatus == MemberStatus.INACTIVE) {
            throw new MemberException(MemberErrorCode.ALREADY_DELETED);
        }
        this.accountStatus = MemberStatus.INACTIVE;
    }

    public void updateIsAdditionInfo(boolean isAdditionalInfo) {
        this.isAdditionalInfo = isAdditionalInfo;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

//    public void updateLastLoginAt(LocalDateTime lastLoginAt) {
//        this.lastLoginAt = lastLoginAt;
//    }


    @Override
    public String getLoginId() {
        return loginId;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Role getRole() {
        return role;
    }


}