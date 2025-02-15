package com.example.fitpassserver.admin.dashboard.service;

import com.example.fitpassserver.admin.dashboard.converter.DashBoardConverter;
import com.example.fitpassserver.admin.dashboard.entity.DashBoard;
import com.example.fitpassserver.admin.dashboard.repository.DashBoardRepository;
import com.example.fitpassserver.admin.dashboard.util.VisitorUtil;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
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
        visitorUtil.initializePageView();
    }


    @Override
    public DashBoard createDashBoardData() {
        LocalDate date = LocalDate.now();
        LocalDateTime greaterThan = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime lessThan = LocalDateTime.of(date, LocalTime.MAX);
        int newMemberCount = memberRepository.countAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(greaterThan, lessThan);
        int usePass = memberFitnessRepository.countAllByActiveTimeGreaterThanEqualAndActiveTimeLessThan(greaterThan, lessThan);
        int buyPass = memberFitnessRepository.countAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(greaterThan, lessThan);
        DashBoard dashboard = dashBoardRepository.save(
                DashBoardConverter.toDashBoard(
                        date,
                        newMemberCount,
                        getVisitCount(),
                        getPageView(),
                        buyPass,
                        usePass
                )
        );
        log.info("Admin DashBoard: Dashboard 생성({})", date);
        return dashboard;
    }

    @Override
    public Page<DashBoard> getDashboards(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        return getDashboardsWithPaging(page, size, sort);
    }

    private Page<DashBoard> getDashboardsWithPaging(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return dashBoardRepository.findAll(pageable);
    }

    private int getVisitCount() {
        return visitorUtil.getVisitCount();
    }

    private int getPageView() {
        return visitorUtil.getPageView();
    }

}
