package com.fitzone.service;

import com.fitzone.model.Member;
import com.fitzone.model.MemberBmr;
import com.fitzone.repository.FileMemberBmrRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BmrService {

    private final FileMemberBmrRepository bmrRepository;

    public BmrService(FileMemberBmrRepository bmrRepository) {
        this.bmrRepository = bmrRepository;
    }

    public void saveBmr(MemberBmr bmr) {
        if (bmr.getUpdatedAt() == null) {
            bmr.setUpdatedAt(LocalDateTime.now());
        }
        bmrRepository.saveBmr(bmr);
    }

    public Optional<MemberBmr> getLatestBmr(String memberId) {
        return bmrRepository.findLatestByMemberId(memberId);
    }

    public List<MemberBmr> getBmrHistory(String memberId) {
        return bmrRepository.findByMemberId(memberId);
    }

    /**
     * Calculates BMR and updates the given MemberBmr object.
     * Fallback/Validation in case frontend JS is bypassed.
     */
    public void calculateAndUpdate(MemberBmr bmr, Member member) {
        if (member == null) return;
        
        int age = member.getAge();
        String gender = member.getGender();
        double weight = bmr.getWeight();
        double height = bmr.getHeight();
        
        // Mifflin-St Jeor Equation
        double bmrValue = (10 * weight) + (6.25 * height) - (5 * age);
        if ("Male".equalsIgnoreCase(gender)) {
            bmrValue += 5;
        } else {
            bmrValue -= 161;
        }
        
        bmr.setBmr((int) Math.round(bmrValue));
        
        // Activity Multiplier
        double multiplier = 1.2;
        switch (bmr.getActivityLevel() != null ? bmr.getActivityLevel() : "") {
            case "Lightly Active": multiplier = 1.375; break;
            case "Moderately Active": multiplier = 1.55; break;
            case "Very Active": multiplier = 1.725; break;
            case "Extra Active": multiplier = 1.9; break;
            case "Sedentary":
            default: multiplier = 1.2; break;
        }
        
        int maintenance = (int) Math.round(bmrValue * multiplier);
        bmr.setMaintenanceCalories(maintenance);
        bmr.setWeightLossCalories(maintenance - 500);
        bmr.setWeightGainCalories(maintenance + 500);
        
        // Protein & Water
        bmr.setProteinTarget((int) Math.round(weight * 2.2));
        bmr.setWaterTarget((int) Math.round(weight * 0.033));
        
        bmr.setUpdatedAt(LocalDateTime.now());
    }
}
