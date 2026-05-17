package com.fitzone.model;

public class AiDietPlanRequest {
    private String dietType;
    private int mealsPerDay;
    private String allergies;
    private String medicalConditions;
    private String budgetPreference;
    private String localFoodPreference;

    public AiDietPlanRequest() {}

    public String getDietType() { return dietType; }
    public void setDietType(String dietType) { this.dietType = dietType; }

    public int getMealsPerDay() { return mealsPerDay; }
    public void setMealsPerDay(int mealsPerDay) { this.mealsPerDay = mealsPerDay; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getMedicalConditions() { return medicalConditions; }
    public void setMedicalConditions(String medicalConditions) { this.medicalConditions = medicalConditions; }

    public String getBudgetPreference() { return budgetPreference; }
    public void setBudgetPreference(String budgetPreference) { this.budgetPreference = budgetPreference; }

    public String getLocalFoodPreference() { return localFoodPreference; }
    public void setLocalFoodPreference(String localFoodPreference) { this.localFoodPreference = localFoodPreference; }
}
