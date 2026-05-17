package com.fitzone.model;

import java.time.LocalDateTime;

public class MemberBmr {
    private String memberId;
    private double height; // cm
    private double weight; // kg
    private String activityLevel;
    private String fitnessGoal;
    private int bmr;
    private int maintenanceCalories;
    private int weightLossCalories;
    private int weightGainCalories;
    private int proteinTarget; // grams
    private int waterTarget; // liters (or ml)
    private LocalDateTime updatedAt;

    public MemberBmr() {
    }

    public MemberBmr(String memberId, double height, double weight, String activityLevel, String fitnessGoal,
                     int bmr, int maintenanceCalories, int weightLossCalories, int weightGainCalories,
                     int proteinTarget, int waterTarget, LocalDateTime updatedAt) {
        this.memberId = memberId;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.fitnessGoal = fitnessGoal;
        this.bmr = bmr;
        this.maintenanceCalories = maintenanceCalories;
        this.weightLossCalories = weightLossCalories;
        this.weightGainCalories = weightGainCalories;
        this.proteinTarget = proteinTarget;
        this.waterTarget = waterTarget;
        this.updatedAt = updatedAt;
    }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public String getFitnessGoal() { return fitnessGoal; }
    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }

    public int getBmr() { return bmr; }
    public void setBmr(int bmr) { this.bmr = bmr; }

    public int getMaintenanceCalories() { return maintenanceCalories; }
    public void setMaintenanceCalories(int maintenanceCalories) { this.maintenanceCalories = maintenanceCalories; }

    public int getWeightLossCalories() { return weightLossCalories; }
    public void setWeightLossCalories(int weightLossCalories) { this.weightLossCalories = weightLossCalories; }

    public int getWeightGainCalories() { return weightGainCalories; }
    public void setWeightGainCalories(int weightGainCalories) { this.weightGainCalories = weightGainCalories; }

    public int getProteinTarget() { return proteinTarget; }
    public void setProteinTarget(int proteinTarget) { this.proteinTarget = proteinTarget; }

    public int getWaterTarget() { return waterTarget; }
    public void setWaterTarget(int waterTarget) { this.waterTarget = waterTarget; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String toCsvLine() {
        return String.join(",",
                safe(memberId),
                String.valueOf(height),
                String.valueOf(weight),
                safe(activityLevel),
                safe(fitnessGoal),
                String.valueOf(bmr),
                String.valueOf(maintenanceCalories),
                String.valueOf(weightLossCalories),
                String.valueOf(weightGainCalories),
                String.valueOf(proteinTarget),
                String.valueOf(waterTarget),
                updatedAt != null ? updatedAt.toString() : ""
        );
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
