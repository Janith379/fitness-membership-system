package com.fitzone.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitzone.model.AiDietPlanResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Repository
public class FileAiDietPlanRepository {

    @Value("${fitzone.data.ai-plans-dir}")
    private String aiPlansDir;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            Path dirPath = Paths.get(aiPlansDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        } catch (IOException e) {
            System.err.println("Error creating AI plans directory: " + e.getMessage());
        }
    }

    public void saveAiDietPlan(String memberId, AiDietPlanResponse plan) {
        try {
            File file = new File(aiPlansDir + "/" + memberId + "_ai_plan.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, plan);
        } catch (IOException e) {
            System.err.println("Error saving AI diet plan for member " + memberId + ": " + e.getMessage());
        }
    }

    public Optional<AiDietPlanResponse> getAiDietPlan(String memberId) {
        try {
            File file = new File(aiPlansDir + "/" + memberId + "_ai_plan.json");
            if (file.exists()) {
                AiDietPlanResponse plan = objectMapper.readValue(file, AiDietPlanResponse.class);
                return Optional.of(plan);
            }
        } catch (IOException e) {
            System.err.println("Error reading AI diet plan for member " + memberId + ": " + e.getMessage());
        }
        return Optional.empty();
    }
}
