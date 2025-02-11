package com.example.fitpassserver.admin.dashboard.service;

import com.example.fitpassserver.admin.dashboard.converter.DashBoardConverter;
import com.example.fitpassserver.admin.dashboard.entity.DashBoard;
import com.example.fitpassserver.admin.dashboard.repository.DashBoardRepository;
import com.example.fitpassserver.admin.dashboard.util.VisitorUtil;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DashboardAdminServiceImpl implements DashboardAdminService {

    private final DashBoardRepository dashBoardRepository;
    private final MemberRepository memberRepository;
    private final MemberFitnessRepository memberFitnessRepository;
    private final VisitorUtil visitorUtil;

    @Scheduled(cron = "59 59 23 * * ?") // 매일 23:59:59에 실행
    public void schedule() {
        createDashBoardData();
        visitorUtil.clear();
    }


    @Override
    public DashBoard createDashBoardData() {
        LocalDate date = LocalDate.now();
        LocalDateTime greaterThan = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime lessThan = LocalDateTime.of(date, LocalTime.MAX);
        int newMemberCount = memberRepository.countAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(greaterThan, lessThan);
        int usePass = memberFitnessRepository.countAllByActiveTimeGreaterThanEqualAndActiveTimeLessThan(greaterThan, lessThan);
        int buyPass = memberFitnessRepository.countAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(greaterThan, lessThan);
        return dashBoardRepository.save(
                DashBoardConverter.toDashBoard(
                        date,
                        newMemberCount,
                        getVisitCount(),
                        getPageView(),
                        buyPass,
                        usePass
                )
        );
    }

    private int getVisitCount() {
        return visitorUtil.getVisitCount();
    }

    // TODO: 구현 예정
    private int getPageView() {
        return 0;
    }

}
