package com.fitzone.repository;

import com.fitzone.model.MembershipPlan;
import com.fitzone.util.CsvUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileMembershipPlanRepository {

    @Value("${fitzone.data.membership-plans-file}")
    private String plansFilePath;

    @PostConstruct
    public void init() {
        CsvUtil.ensureFileExists(plansFilePath, CsvUtil.MEMBERSHIP_PLANS_HEADER);
    }

    public void savePlan(MembershipPlan plan) {
        CsvUtil.appendLine(plansFilePath, plan.toCsvLine());
    }

    public List<MembershipPlan> getAllPlans() {
        List<String> lines = CsvUtil.readDataLines(plansFilePath);
        List<MembershipPlan> plans = new ArrayList<>();
        for (String line : lines) {
            MembershipPlan p = CsvUtil.parseMembershipPlanLine(line);
            if (p != null) {
                plans.add(p);
            }
        }
        return plans;
    }

    public Optional<MembershipPlan> findById(String planId) {
        return getAllPlans().stream()
                .filter(p -> p.getPlanId().equalsIgnoreCase(planId))
                .findFirst();
    }

    public void updatePlan(MembershipPlan updatedPlan) {
        List<MembershipPlan> allPlans = getAllPlans();
        List<String> updatedLines = new ArrayList<>();
        for (MembershipPlan p : allPlans) {
            if (p.getPlanId().equalsIgnoreCase(updatedPlan.getPlanId())) {
                updatedLines.add(updatedPlan.toCsvLine());
            } else {
                updatedLines.add(p.toCsvLine());
            }
        }
        CsvUtil.rewriteFile(plansFilePath, CsvUtil.MEMBERSHIP_PLANS_HEADER, updatedLines);
    }

    public void deletePlan(String planId) {
        List<MembershipPlan> allPlans = getAllPlans();
        List<String> remainingLines = allPlans.stream()
                .filter(p -> !p.getPlanId().equalsIgnoreCase(planId))
                .map(MembershipPlan::toCsvLine)
                .collect(Collectors.toList());
        CsvUtil.rewriteFile(plansFilePath, CsvUtil.MEMBERSHIP_PLANS_HEADER, remainingLines);
    }
}
