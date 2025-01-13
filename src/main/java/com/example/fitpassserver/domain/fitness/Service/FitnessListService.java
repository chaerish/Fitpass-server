package com.example.fitpassserver.domain.fitness.Service;

import com.example.fitpassserver.domain.fitness.Controller.response.CursorPaginationResponse;
import com.example.fitpassserver.domain.fitness.Controller.response.FitnessListResponse;
import com.example.fitpassserver.domain.fitness.Repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.Util.DistanceCalculator;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitnessListService {
    private final FitnessRepository fitnessRepository;

    public FitnessListService(FitnessRepository fitnessRepository) {
        this.fitnessRepository = fitnessRepository;
    }

    public CursorPaginationResponse<FitnessListResponse> getFitnessList(
            String categoryName,
            String sort,
            double userLatitude,
            double userLongitude,
            Long cursor,
            int size
    ) {
        List<Fitness> fitnessList = fetchFitnessListBySort(categoryName, sort, cursor, size);

        List<FitnessListResponse> responses = fitnessList.stream()
                .map(fitness -> {
                    double distance = DistanceCalculator.distance(
                            userLatitude, userLongitude,
                            fitness.getLatitude(), fitness.getLongitude()
                    );
                    String categories = fitness.getCategoryList().stream()
                            .map(Category::getCategoryName)
                            .collect(Collectors.joining(", "));

                    return new FitnessListResponse(
                            fitness.getId(),
                            fitness.getName(),
                            fitness.getAddress(),
                            distance,
                            fitness.getFee(),
                            categories
                    );
                })
                .collect(Collectors.toList());

        Long nextCursor = fitnessList.isEmpty() ? null : fitnessList.get(fitnessList.size() - 1).getId();
        return new CursorPaginationResponse<>(responses, nextCursor);
    }
    private List<Fitness> fetchFitnessListBySort(String categoryName, String sort, Long cursor, int size) {
        Sort sortingCriteria = switch (sort) {
            case "lowPrice" -> Sort.by(Sort.Direction.ASC, "fee");
            case "highPrice" -> Sort.by(Sort.Direction.DESC, "fee");
            default -> Sort.by(Sort.Direction.ASC, "distance"); // 기본값: 거리순 정렬
        };
        // Pageable 객체 생성 (커서와 페이지 크기 반영)
        Pageable pageable = PageRequest.of(0, size, sortingCriteria);

        // 커서 기준 데이터 조회
        if (cursor != null) {
            return fitnessRepository.findByCategoryList_CategoryNameAndIdGreaterThan(
                    categoryName, cursor, pageable
            ).getContent();
        } else {
            return fitnessRepository.findByCategoryList_CategoryName(
                    categoryName, pageable
            ).getContent();
        }
    }
}