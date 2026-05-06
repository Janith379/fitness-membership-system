package com.fitzone.service;

import com.fitzone.model.DietPlan;
import com.fitzone.repository.FileDietPlanRepository;
import com.fitzone.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DietPlanService {

    private final FileDietPlanRepository dietPlanRepository;
    private final IdGenerator idGenerator;

    public DietPlanService(FileDietPlanRepository dietPlanRepository, IdGenerator idGenerator) {
        this.dietPlanRepository = dietPlanRepository;
        this.idGenerator = idGenerator;
    }

    public DietPlan createDietPlan(DietPlan dietPlan) {
        dietPlan.setDietId(idGenerator.generateDietId());
        dietPlanRepository.saveDietPlan(dietPlan);
        return dietPlan;
    }

    public List<DietPlan> getAllDietPlans() {
        return dietPlanRepository.getAllDietPlans();
    }

    public Optional<DietPlan> findById(String dietId) {
        return dietPlanRepository.findById(dietId);
    }
    
    public List<DietPlan> findByMemberId(String memberId) {
        return dietPlanRepository.findByMemberId(memberId);
    }

    public void updateDietPlan(String dietId, DietPlan updatedDietPlan) {
        Optional<DietPlan> existing = dietPlanRepository.findById(dietId);
        if (existing.isPresent()) {
            updatedDietPlan.setDietId(dietId);
            dietPlanRepository.updateDietPlan(updatedDietPlan);
        }
    }

    public void deleteDietPlan(String dietId) {
        dietPlanRepository.deleteDietPlan(dietId);
    }
}
