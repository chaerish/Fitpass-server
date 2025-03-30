package com.example.fitpassserver.domain.fitness.entity;

import com.example.fitpassserver.admin.fitness.converter.FitnessAdminConverter;
import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "etc")
    private String etc;

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

    public void update(FitnessAdminRequestDTO.FitnessReqDTO dto, List<Category> categoryList) {
        if(dto.getTotalFee() > dto.getFee()){
            throw new FitnessException(FitnessErrorCode.INVALID_SALE_PRICE);
        }
        this.name = dto.getFitnessName();
        this.address = dto.getAddress();
        this.phoneNumber = dto.getPhoneNumber();
        this.fee = dto.getFee();
        this.totalFee = dto.getTotalFee();
        this.categoryList = categoryList;
        this.isPurchasable = dto.isPurchasable();
        this.notice = dto.getNotice();
        this.time = FitnessAdminConverter.convertMapToFormattedString(dto.getTime());
        this.howToUse = dto.getHowToUse();
        this.etc = dto.getEtc();
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
    }

}
