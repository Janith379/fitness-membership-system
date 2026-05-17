package com.fitzone.repository;

import com.fitzone.model.DietPlan;
import com.fitzone.util.CsvUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileDietPlanRepository {

    @Value("${fitzone.data.diet-plans-file}")
    private String dietPlansFilePath;

    @PostConstruct
    public void init() {
        CsvUtil.ensureFileExists(dietPlansFilePath, CsvUtil.DIET_PLANS_HEADER);
    }

    public void saveDietPlan(DietPlan dietPlan) {
        CsvUtil.appendLine(dietPlansFilePath, dietPlan.toCsvLine());
    }

    public List<DietPlan> getAllDietPlans() {
        List<String> lines = CsvUtil.readDataLines(dietPlansFilePath);
        List<DietPlan> dietPlans = new ArrayList<>();
        for (String line : lines) {
            DietPlan dp = CsvUtil.parseDietPlanLine(line);
            if (dp != null) {
                dietPlans.add(dp);
            }
        }
        return dietPlans;
    }

    public Optional<DietPlan> findById(String dietId) {
        return getAllDietPlans().stream()
                .filter(dp -> dp.getDietId().equalsIgnoreCase(dietId))
                .findFirst();
    }
    
    public List<DietPlan> findByMemberId(String memberId) {
        return getAllDietPlans().stream()
                .filter(dp -> dp.getMemberId().equalsIgnoreCase(memberId))
                .collect(Collectors.toList());
    }

    public void updateDietPlan(DietPlan updatedDietPlan) {
        List<DietPlan> allDietPlans = getAllDietPlans();
        List<String> updatedLines = new ArrayList<>();
        for (DietPlan dp : allDietPlans) {
            if (dp.getDietId().equalsIgnoreCase(updatedDietPlan.getDietId())) {
                updatedLines.add(updatedDietPlan.toCsvLine());
            } else {
                updatedLines.add(dp.toCsvLine());
            }
        }
        CsvUtil.rewriteFile(dietPlansFilePath, CsvUtil.DIET_PLANS_HEADER, updatedLines);
    }

    public void deleteDietPlan(String dietId) {
        List<DietPlan> allDietPlans = getAllDietPlans();
        List<String> remainingLines = allDietPlans.stream()
                .filter(dp -> !dp.getDietId().equalsIgnoreCase(dietId))
                .map(DietPlan::toCsvLine)
                .collect(Collectors.toList());
        CsvUtil.rewriteFile(dietPlansFilePath, CsvUtil.DIET_PLANS_HEADER, remainingLines);
    }
}
