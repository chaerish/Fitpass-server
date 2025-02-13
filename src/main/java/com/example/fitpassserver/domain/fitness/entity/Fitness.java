package com.example.fitpassserver.domain.fitness.entity;

import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
    }

    public void setMainImage(String mainImageKey){
        this.fitnessImage = mainImageKey;
    }

    public void setAdditionalImages(List<FitnessImage> fitnessImageList){
        this.additionalImages = fitnessImageList;
    }
}
