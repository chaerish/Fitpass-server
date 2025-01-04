package com.example.fitpassserver.domain.fitness.entity;

import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private String fitnessName;

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


}
