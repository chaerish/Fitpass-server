package com.example.fitpassserver.domain.plan.service;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import com.example.fitpassserver.domain.plan.dto.response.SubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import java.time.LocalDate;
import java.util.List;
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
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final SmsCertificationUtil smsCertificationUtil;
    private final CoinService coinService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void regularPay() {
        List<Plan> planToProcess = planRepository.findAllByPlanDateLessThanEqual(LocalDate.now().minusMonths(1));
        for (Plan plan : planToProcess) {
            try {
                processPay(plan);
            } catch (PlanException e) {
                if (e.getBaseErrorCode() == PlanErrorCode.PLAN_INSUFFICIENT_FUNDS) {
                    log.error("plan ID: {} - 잔액 부족으로 결제 실패: {}", plan.getId(), e.getMessage(), e);
                    if (plan.isTargetForCancel()) {
                        cancel(plan);
                    } else {
                        insufficient(plan);
                    }
                } else {
                    fail(plan);
                    log.error("plan ID: {} - 카카오페이 결제 오류", plan.getId(), e);
                }
            } catch (Exception e) {
                log.error("plan ID: {} - 예상치 못한 결제 오류 발생", plan.getId(), e);
            }
        }
    }

    @Transactional
    public void processPay(Plan plan) throws PlanException {
        Member member = plan.getMember();
        SubscriptionResponseDTO response = paymentService.request(plan);
        CoinPaymentHistory history = coinPaymentHistoryService.createNewCoinPaymentByScheduler(member, response);
        updatePlanInfo(plan);
        Coin coin = coinService.createSubscriptionNewCoin(plan.getMember(), history, plan);
        coinPaymentHistoryService.approve(history, coin);
        planRepository.save(plan);
    }

    private static void updatePlanInfo(Plan plan) {
        plan.updatePlanDate();
        plan.setPaymentStatus(PaymentStatus.SUCCESS);
        plan.addPaymentCount();
    }

    @Transactional
    public void insufficient(Plan plan) {
        plan.setPaymentStatus(PaymentStatus.INSUFFICIENT);
        planRepository.save(plan);
    }

    @Transactional
    public void cancel(Plan plan) {
        plan.setPaymentStatus(PaymentStatus.CANCEL);
        paymentService.cancelSubscription(plan.getMember());
        plan.changePlanType(PlanType.NONE);
        smsCertificationUtil.sendPlanCancelAlert(plan.getMember().getPhoneNumber(), plan.getPlanType().getName());
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
        List<Plan> planToProcess = planRepository.findAllByPlanDateLessThanEqual(LocalDate.now().minusMonths(1));
        for (Plan plan : planToProcess) {
            try {
                processPay(plan);
            } catch (PlanException e) {
                if (e.getBaseErrorCode() == PlanErrorCode.PLAN_INSUFFICIENT_FUNDS) {
                    insufficient(plan);
                    smsCertificationUtil.sendPlanInsufficientFundsAlert(
                            plan.getMember().getPhoneNumber(),
                            plan.getPlanType().getName());
                    log.error("plan ID: {} - 잔액 부족으로 결제 실패: {}", plan.getId(), e.getMessage(), e);
                } else {
                    fail(plan);
                    log.error("plan ID: {} - 카카오페이 결제 오류", plan.getId(), e);
                }
            } catch (Exception e) {
                log.error("plan ID: {} - 예상치 못한 결제 오류 발생", plan.getId(), e);
            }
        }
    }

}
