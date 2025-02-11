package com.example.fitpassserver.domain.fitness.service.command;

import com.example.fitpassserver.domain.coin.converter.CoinUsageHistoryConverter;
import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinUsageHistory;
import com.example.fitpassserver.domain.coin.exception.CoinUsageHistoryErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinUsageHistoryException;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coin.repository.CoinUsageHistoryRepository;
import com.example.fitpassserver.domain.fitness.converter.MemberFitnessConverter;
import com.example.fitpassserver.domain.fitness.dto.MemberFitnessRequestDTO;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.exception.MemberFitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.MemberFitnessException;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import com.example.fitpassserver.domain.fitnessPaymentHistory.converter.FitnessPaymentHistoryConverter;
import com.example.fitpassserver.domain.fitnessPaymentHistory.entity.FitnessPaymentHistory;
import com.example.fitpassserver.domain.fitnessPaymentHistory.exception.FitnessPaymentHistoryErrorCode;
import com.example.fitpassserver.domain.fitnessPaymentHistory.exception.FitnessPaymentHistoryException;
import com.example.fitpassserver.domain.fitnessPaymentHistory.repository.FitnessPaymentHistoryRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberFitnessCommandServiceImpl implements MemberFitnessCommandService {

    private static final Duration PASS_ACTIVE_DURATION = Duration.ofHours(1L);
    private static final Duration COIN_ACTIVE_DURATION = Duration.ofDays(30L);

    private final MemberFitnessRepository memberFitnessRepository;
    private final FitnessRepository fitnessRepository;
    private final FitnessPaymentHistoryRepository fitnessPaymentHistoryRepository;
    private final CoinRepository coinRepository;
    private final CoinUsageHistoryRepository coinUsageHistoryRepository;
    private final SmsCertificationUtil smsCertificationUtil;

    @Override
    @Transactional
    public MemberFitness buyFitness(Member member, MemberFitnessRequestDTO.CreateMemberFitnessRequestDTO dto) {
        Fitness fitness = fitnessRepository.findById(dto.getFitnessId()).orElseThrow(() ->
                new FitnessException(FitnessErrorCode.PASS_NOT_FOUND));

        // 이미 구매한 패스가 존재하는 경우 예외 처리
//        if (memberFitnessRepository.existsByMemberAndStatusIsNot(member, Status.DONE)) {
//            throw new MemberFitnessException(MemberFitnessErrorCode.EXIST_PASS);
//        }

        MemberFitness memberFitness = createMemberFitness(member, fitness, dto);

        // FitnessPaymentHistory 생성
        FitnessPaymentHistory fitnessPaymentHistory = createFitnessPaymentHistory(memberFitness);

        // 코인 차감
        // 코인 사용 기록 저장
        Long price = (long) (fitness.getFee() - fitness.getDiscount());
        List<CoinUsageHistory> coinUsageHistories = useCoin(member, fitnessPaymentHistory, price);

        // 결제 문자 발송
        smsCertificationUtil.sendPassPaymentSMS(member.getPhoneNumber(), price, memberFitness);

        return memberFitness;
    }

    @Override
    @Transactional
    public MemberFitness cancelFitness(Member member, MemberFitnessRequestDTO.CancelMemberFitnessRequestDTO dto) {

        MemberFitness memberFitness = memberFitnessRepository.findById(dto.getMemberFitnessId()).orElseThrow(() ->
                new MemberFitnessException(MemberFitnessErrorCode.NOT_FOUND));

        // 피트니스 패스가 본인 것이지 확인
        if (!memberFitness.getMember().getId().equals(member.getId())) {
            throw new FitnessException(FitnessErrorCode.USER_MISMATCH);
        }

        // 활성화 여부 확인
        LocalDateTime activeTime = memberFitness.getActiveTime();
        if (memberFitness.getStatus().equals(Status.PROGRESS)) {
            throw new MemberFitnessException(MemberFitnessErrorCode.ALREADY_IN_PROGRESS);
        } else if (!memberFitness.getStatus().equals(Status.NONE) && activeTime.isBefore(LocalDateTime.now().minus(PASS_ACTIVE_DURATION))) {
            throw new MemberFitnessException(MemberFitnessErrorCode.ALREADY_USED_PASS);
        }

        FitnessPaymentHistory fitnessPaymentHistory = fitnessPaymentHistoryRepository.findByMemberFitness(memberFitness).orElseThrow(() ->
                new FitnessPaymentHistoryException(FitnessPaymentHistoryErrorCode.NOT_FOUND));

        List<CoinUsageHistory> usageHistories = coinUsageHistoryRepository.findAllByFitnessPaymentHistory(fitnessPaymentHistory);
        if (usageHistories == null || usageHistories.isEmpty()) {
            throw new CoinUsageHistoryException(CoinUsageHistoryErrorCode.NOT_FOUND);
        }

        // rollback
        rollbackCoin(usageHistories);
        deleteFitnessPaymentHistory(fitnessPaymentHistory);
        rollbackMemberFitness(memberFitness);

        return memberFitness;
    }

    private MemberFitness createMemberFitness(Member member, Fitness fitness, MemberFitnessRequestDTO.CreateMemberFitnessRequestDTO dto) {
        if (!dto.isAgree()) {
            throw new MemberFitnessException(MemberFitnessErrorCode.MEMBER_FITNESS_POLICY_NOT_AGREE);
        }

        // 멤버 피트니스 저장
        return memberFitnessRepository.save(MemberFitnessConverter.toEntity(member, fitness, dto));
    }

    private FitnessPaymentHistory createFitnessPaymentHistory(MemberFitness memberFitness) {
        FitnessPaymentHistory fitnessPaymentHistory = FitnessPaymentHistoryConverter.toEntity(memberFitness);
        return fitnessPaymentHistoryRepository.save(fitnessPaymentHistory);
    }

    private List<CoinUsageHistory> useCoin(Member member, FitnessPaymentHistory fitnessPaymentHistory, Long price) {
        List<Coin> coins = coinRepository.findAllByMemberIsAndCountGreaterThanAndExpiredDateGreaterThanOrderByExpiredDateAsc(member, 1L, LocalDate.now().minusDays(COIN_ACTIVE_DURATION.toDays()));

        // 보유한 코인 확인
        if (coins.stream().map(Coin::getCount).mapToLong(Long::intValue).sum() < price) {
            throw new MemberFitnessException(MemberFitnessErrorCode.NOT_ENOUGH_COIN);
        }

        // 사용 기록 저장 공간
        List<CoinUsageHistory> usages = new ArrayList<>();

        // 코인 차감 및 사용 기록 생성
        for (Coin coin : coins) {
            Long count = coin.getCount();
            CoinUsageHistory coinUsageHistory;
            if (count > price) {
                coin.decreaseCount(price);
                coinUsageHistory = CoinUsageHistoryConverter.toCoinUsageHistory(coin, fitnessPaymentHistory, price.intValue());
                usages.add(coinUsageHistory);
                break;
            } else {
                coin.decreaseCount(count);
                coinUsageHistory = CoinUsageHistoryConverter.toCoinUsageHistory(coin, fitnessPaymentHistory, count.intValue());
                usages.add(coinUsageHistory);
            }
            price -= count;
        }

        return coinUsageHistoryRepository.saveAll(usages);
    }

    private void rollbackCoin(List<CoinUsageHistory> coinUsageHistories) {
        coinUsageHistories.forEach(coinUsageHistory -> {
            Coin coin = coinUsageHistory.getCoin();
            coin.increaseCount((long) coinUsageHistory.getUsageCount());
        });
        coinUsageHistoryRepository.deleteAll(coinUsageHistories);
    }

    private void deleteFitnessPaymentHistory(FitnessPaymentHistory fitnessPaymentHistory) {
        fitnessPaymentHistoryRepository.delete(fitnessPaymentHistory);
    }

    private void rollbackMemberFitness(MemberFitness memberFitness) {
        memberFitnessRepository.delete(memberFitness);
    }
}