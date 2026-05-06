package com.fitzone.service;

import com.fitzone.model.MembershipPlan;
import com.fitzone.repository.FileMembershipPlanRepository;
import com.fitzone.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipPlanService {

    private final FileMembershipPlanRepository planRepository;
    private final IdGenerator idGenerator;

    public MembershipPlanService(FileMembershipPlanRepository planRepository, IdGenerator idGenerator) {
        this.planRepository = planRepository;
        this.idGenerator = idGenerator;
    }

    public MembershipPlan createPlan(MembershipPlan plan) {
        plan.setPlanId(idGenerator.generatePlanId());
        planRepository.savePlan(plan);
        return plan;
    }

    public List<MembershipPlan> getAllPlans() {
        return planRepository.getAllPlans();
    }

    public Optional<MembershipPlan> findById(String planId) {
        return planRepository.findById(planId);
    }

    public void updatePlan(String planId, MembershipPlan updatedPlan) {
        Optional<MembershipPlan> existing = planRepository.findById(planId);
        if (existing.isPresent()) {
            updatedPlan.setPlanId(planId);
            planRepository.updatePlan(updatedPlan);
        }
    }

    public void deletePlan(String planId) {
        planRepository.deletePlan(planId);
    }
}
