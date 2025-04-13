package com.example.fitpassserver.domain.plan.service;

import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.command.PGPaymentCommandService;
import com.example.fitpassserver.domain.kakaoNotice.util.KakaoAlimtalkUtil;
import com.example.fitpassserver.domain.plan.entity.PaymentType;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlanScheduler {
    private final PlanRepository planRepository;
    private final KakaoPaymentService paymentService;
    private final KakaoAlimtalkUtil kakaoAlimtalkUtil;
    private final PGPaymentCommandService paymentCommandService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void regularPay() {
        List<Plan> planToProcess = planRepository.findAllByPlanDateLessThanEqualAndPlanTypeIsNot(
                LocalDate.now().minusMonths(1), PlanType.NONE);
        for (Plan plan : planToProcess) {
            if (plan.getPaymentType().equals(PaymentType.KAKAO)) {
                kakaoSubscriptionPay(plan, this::handleException);
            }
            else if (plan.getPaymentType().equals(PaymentType.PG)) {
                pgSubscriptionPay(plan, this::handleException);
            }
        }
    }


    @Transactional
    public void insufficient(Plan plan) {
        plan.setPaymentStatus(PaymentStatus.INSUFFICIENT);
        planRepository.save(plan);
    }

    @Transactional
    public void cancel(Plan plan) {
        plan.setPaymentStatus(PaymentStatus.CANCEL);
        paymentService.cancelSubscription(plan);
        plan.changePlanType(PlanType.NONE);
//        smsCertificationUtil.sendPlanCancelAlert(plan.getMember().getPhoneNumber(), plan.getPlanType().getName());
//        kakaoAlimtalkUtil.paymentSecondFailAlimtalk(plan.getMember().getPhoneNumber());
        planRepository.save(plan);
    }

    @Transactional
    public void fail(Plan plan) {
        plan.setPaymentStatus(PaymentStatus.FAIL);
        planRepository.save(plan);
    }


    @Scheduled(cron = "0 0 9 * * ?")
    @Async
    public void retryRegularPay() {
        List<Plan> planToProcess = planRepository.findAllByPlanDateLessThanEqualAndPlanTypeIsNot(
                LocalDate.now().minusMonths(1), PlanType.NONE);
        for (Plan plan : planToProcess) {
            if (plan.getPaymentType().equals(PaymentType.KAKAO)) {
                kakaoSubscriptionPay(plan, this::retryHandleException);
            }
            else if (plan.getPaymentType().equals(PaymentType.PG)) {
                pgSubscriptionPay(plan, this::retryHandleException);
            }
        }
    }

    private void kakaoSubscriptionPay(Plan plan, BiConsumer<Plan, PlanException> handleException) {
        try {
            paymentService.subscribe(plan);
        } catch (PlanException e) {
            handleException.accept(plan, e);
        } catch (Exception e) {
            log.error("plan ID: {} - 예상치 못한 결제 오류 발생", plan.getId(), e);
        }
    }

    private void pgSubscriptionPay(Plan plan, BiConsumer<Plan, PlanException> handleException) {
        try {
            paymentCommandService.paySubscription(plan);
        } catch (PlanException e) {
            handleException.accept(plan, e);
        } catch (Exception e) {
            log.error("plan ID: {} - 예상치 못한 결제 오류 발생", plan.getId(), e);
        }
    }

    private void handleException(Plan plan, PlanException e) {
        if (e.getBaseErrorCode() == PlanErrorCode.PLAN_INSUFFICIENT_FUNDS) {
            log.error("plan ID: {} - 잔액 부족으로 결제 실패: {}", plan.getId(), e.getMessage(), e);
            if (plan.isTargetForCancel()) {
                cancel(plan);
            } else {
                insufficient(plan);
            }
        } else {
            fail(plan);
            log.error("plan ID: {} - 결제사 결제 오류", plan.getId(), e);
        }
    }

    private void retryHandleException(Plan plan, PlanException e) {
        if (e.getBaseErrorCode() == PlanErrorCode.PLAN_INSUFFICIENT_FUNDS) {
            insufficient(plan);
            kakaoAlimtalkUtil.sendFirstPaymentFail(
                    plan.getMember().getPhoneNumber()
            );
            log.error("plan ID: {} - 잔액 부족으로 결제 실패: {}", plan.getId(), e.getMessage(), e);
        } else {
            fail(plan);
            log.error("plan ID: {} - 결제 사 결제 오류", plan.getId(), e);
        }
    }

}
