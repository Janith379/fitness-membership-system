package com.fitzone.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AiDietPlanResponse {
    @JsonProperty("member_id")
    private String memberId;

    @JsonProperty("target_calories")
    private int targetCalories;

    @JsonProperty("total_calories")
    private int totalCalories;
    
    private Macros macros;
    
    @JsonProperty("water_intake_liters")
    private double waterIntakeLiters;
    
    @JsonProperty("meal_plan")
    private List<Meal> mealPlan;
    
    @JsonProperty("meal_substitutions")
    private List<Substitution> mealSubstitutions;
    
    @JsonProperty("trainer_alerts")
    private List<String> trainerAlerts;
    
    @JsonProperty("admin_alerts")
    private List<String> adminAlerts;
    
    private List<String> recommendations;

    public AiDietPlanResponse() {}

    // Getters and Setters
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public int getTargetCalories() { return targetCalories; }
    public void setTargetCalories(int targetCalories) { this.targetCalories = targetCalories; }

    public int getTotalCalories() { return totalCalories; }
    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }

    public Macros getMacros() { return macros; }
    public void setMacros(Macros macros) { this.macros = macros; }

    public double getWaterIntakeLiters() { return waterIntakeLiters; }
    public void setWaterIntakeLiters(double waterIntakeLiters) { this.waterIntakeLiters = waterIntakeLiters; }

    public List<Meal> getMealPlan() { return mealPlan; }
    public void setMealPlan(List<Meal> mealPlan) { this.mealPlan = mealPlan; }

    public List<Substitution> getMealSubstitutions() { return mealSubstitutions; }
    public void setMealSubstitutions(List<Substitution> mealSubstitutions) { this.mealSubstitutions = mealSubstitutions; }

    public List<String> getTrainerAlerts() { return trainerAlerts; }
    public void setTrainerAlerts(List<String> trainerAlerts) { this.trainerAlerts = trainerAlerts; }

    public List<String> getAdminAlerts() { return adminAlerts; }
    public void setAdminAlerts(List<String> adminAlerts) { this.adminAlerts = adminAlerts; }

    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }

    // Nested Classes
    public static class Macros {
        @JsonProperty("protein_g")
        private int proteinG;
        
        @JsonProperty("carbs_g")
        private int carbsG;
        
        @JsonProperty("fat_g")
        private int fatG;

        public int getProteinG() { return proteinG; }
        public void setProteinG(int proteinG) { this.proteinG = proteinG; }

        public int getCarbsG() { return carbsG; }
        public void setCarbsG(int carbsG) { this.carbsG = carbsG; }

        public int getFatG() { return fatG; }
        public void setFatG(int fatG) { this.fatG = fatG; }
    }

    public static class Meal {
        private String meal;
        
        @JsonProperty("subtotal_calories")
        private int subtotalCalories;
        
        private List<FoodItem> items;
        
        @JsonProperty("protein_g")
        private int proteinG;
        
        @JsonProperty("carbs_g")
        private int carbsG;
        
        @JsonProperty("fat_g")
        private int fatG;

        public String getMeal() { return meal; }
        public void setMeal(String meal) { this.meal = meal; }

        public int getSubtotalCalories() { return subtotalCalories; }
        public void setSubtotalCalories(int subtotalCalories) { this.subtotalCalories = subtotalCalories; }

        public List<FoodItem> getItems() { return items; }
        public void setItems(List<FoodItem> items) { this.items = items; }

        public int getProteinG() { return proteinG; }
        public void setProteinG(int proteinG) { this.proteinG = proteinG; }

        public int getCarbsG() { return carbsG; }
        public void setCarbsG(int carbsG) { this.carbsG = carbsG; }

        public int getFatG() { return fatG; }
        public void setFatG(int fatG) { this.fatG = fatG; }
    }

    public static class FoodItem {
        private String food;
        private String quantity;
        private int calories;

        public String getFood() { return food; }
        public void setFood(String food) { this.food = food; }

        public String getQuantity() { return quantity; }
        public void setQuantity(String quantity) { this.quantity = quantity; }

        public int getCalories() { return calories; }
        public void setCalories(int calories) { this.calories = calories; }
    }

    public static class Substitution {
        private String replace;
        private String with;

        public String getReplace() { return replace; }
        public void setReplace(String replace) { this.replace = replace; }

        public String getWith() { return with; }
        public void setWith(String with) { this.with = with; }
    }
}
