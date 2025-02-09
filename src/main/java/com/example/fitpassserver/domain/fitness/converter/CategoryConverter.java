package com.example.fitpassserver.domain.fitness.converter;

import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryConverter {
    public static List<Category> toEntityList(List<String> names, Fitness fitness) {
        return names.stream()
                .map(name -> Category.builder()
                        .categoryName(name)
                        .fitness(fitness)
                        .build())
                .collect(Collectors.toList());
    }

}
