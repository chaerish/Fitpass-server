package com.example.fitpassserver.domain.plan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class PlanSchedulerTest {
    @Mock
    private PlanRepository planRepository;

    @Mock
    private KakaoPaymentService paymentService;

    @Mock
    private CoinPaymentHistoryService coinPaymentHistoryService;

    @Mock
    private SmsCertificationUtil smsCertificationUtil;

    @InjectMocks
    private PlanScheduler planScheduler;

    private Plan testPlan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Member member = Member.builder().id(1L).phoneNumber("01020011223").build();
        testPlan = Plan.builder()
                .id(1L)
                .member(member)
                .planDate(LocalDate.now().minusMonths(1))
                .planType(PlanType.BASIC)
                .paymentStatus(PaymentStatus.SUCCESS)
                .paymentCount(1)
                .build();
    }

    @Test
    void 잔액부족() {
        // Given: planRepository에서 1개월 지난 plan 반환
        when(planRepository.findAllByPlanDateLessThanEqual(any())).thenReturn(List.of(testPlan));
        // Given: paymentService.request가 잔액 부족 예외를 발생시킴
        doThrow(new PlanException(PlanErrorCode.PLAN_INSUFFICIENT_FUNDS))
                .when(paymentService).request(any());
        planScheduler.retryRegularPay();
        assertEquals(testPlan.getPaymentStatus(), PaymentStatus.INSUFFICIENT);
        Mockito.verify(planRepository).save(testPlan);
    }
}