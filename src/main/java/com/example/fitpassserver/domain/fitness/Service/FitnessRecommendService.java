package com.example.fitpassserver.domain.fitness.Service;
import com.example.fitpassserver.domain.fitness.Util.DistanceCalculator;
import com.example.fitpassserver.domain.fitness.Repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FitnessRecommendService {
    private final FitnessRepository fitnessRepository;

    public FitnessRecommendService(FitnessRepository fitnessRepository) {
        this.fitnessRepository = fitnessRepository;
    }

    public List<Fitness> getRecommendFitness() {
        return fitnessRepository.findByIsRecommendTrue();
    }

    public List<Map<String, Object>> getRecommendFitnessAsMap(double userLatitude, double userLongitude) {
        List<Fitness> fitnessList = getRecommendFitness();
        return fitnessList.stream()
                .map(fitness -> {

                    double distance = DistanceCalculator.distance(
                            userLatitude, userLongitude,
                            fitness.getLatitude(), fitness.getLongitude()
                    );
                    Map<String, Object> map = new HashMap<>();
                    map.put("fitnessId", fitness.getId());
                    map.put("name", fitness.getName());
                    map.put("distance", distance);
                    return map;
                })
                .collect(Collectors.toList());
    }
}