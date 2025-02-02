package com.example.fitpassserver.domain.coinPaymentHistory.entity;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "coin_payment_history")
public class CoinPaymentHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "isAgree", nullable = false)
    private boolean isAgree;

    @Column(name = "tid", nullable = false)
    private String tid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_price", nullable = false)
    private Integer paymentPrice;

    @Column(name = "coin_count", nullable = false)
    private Long coinCount;
    @OneToOne
    @JoinColumn(name = "coin_id")
    private Coin coin;

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public void changeStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
