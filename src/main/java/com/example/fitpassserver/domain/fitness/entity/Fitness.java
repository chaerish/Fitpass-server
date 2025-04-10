package com.example.fitpassserver.domain.fitness.entity;

import com.example.fitpassserver.admin.fitness.converter.FitnessAdminConverter;
import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.global.entity.BaseEntity;
import com.example.fitpassserver.owner.owner.entity.Owner;
import jakarta.persistence.*;

import com.example.fitpassserver.owner.owner.entity.Owner;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "fitness")
public class Fitness extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fitness_name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "notice")
    private String notice;

    @Column(name = "time")
    private String time;

    @Column(name = "how_to_use")
    private String howToUse;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "fee")
    private Integer fee;

    @Column(name = "fitness_image")
    private String fitnessImage;

    @Column(name = "distance", nullable = false)
    private Double distance;

    @Column(name = "is_recommend", nullable = false)
    private Boolean isRecommend;

    @Column(name = "is_purchasable")
    private Boolean isPurchasable;

    @Column(name = "total_fee")
    private Integer totalFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @OneToMany(mappedBy = "fitness", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FitnessImage> additionalImages = new ArrayList<>();

    @OneToMany(mappedBy = "fitness", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Category> categoryList = new ArrayList<>();


    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public void setMainImage(String mainImageKey) {
        this.fitnessImage = mainImageKey;
    }

    public void setAdditionalImages(List<FitnessImage> fitnessImageList) {
        this.additionalImages = fitnessImageList;
    }

    public void update(
            String fitnessName,
            String address,
            String detailAddress,
            String phoneNumber,
            int fee,
            int totalFee,
            List<Category> categoryList,
            boolean isPurchasable,
            String notice,
            Map<String, String> time,
            String howToUse,
            double latitude,
            double longitude
    ) {
        if (totalFee > fee) {
            throw new FitnessException(FitnessErrorCode.INVALID_SALE_PRICE);
        }
        this.name = fitnessName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.phoneNumber = phoneNumber;
        this.fee = fee;
        this.totalFee = totalFee;
        this.categoryList = categoryList;
        this.isPurchasable = isPurchasable;
        this.notice = notice;
        this.time = FitnessAdminConverter.convertMapToFormattedString(time);
        this.howToUse = howToUse;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public void updatePurchaseStatus() {
        this.isPurchasable = !isPurchasable;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
