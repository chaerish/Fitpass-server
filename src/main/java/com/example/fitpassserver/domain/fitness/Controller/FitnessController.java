package com.example.fitpassserver.domain.fitness.Controller;
import com.example.fitpassserver.domain.fitness.Service.FitnessRecommendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fitness")
public class FitnessController {
    private final FitnessRecommendService FitnessRecommendService;

    public FitnessController(FitnessRecommendService FitnessRecommendService) {
        this.FitnessRecommendService = FitnessRecommendService;
    }
    @GetMapping("/recommend")
    public ResponseEntity<List<Map<String, Object>>> recommendFitness(
            @RequestParam double latitude,
            @RequestParam double longitude)
    {
        List<Map<String, Object>> response = FitnessRecommendService.getRecommendFitnessAsMap(latitude, longitude);
        return ResponseEntity.ok(response);
    }
}