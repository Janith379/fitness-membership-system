package com.fitzone.model;

public class DietPlan {
    private String dietId;
    private String memberId;
    private String mealType;
    private String foodItems;
    private int calories;

    public DietPlan() {}

    public DietPlan(String dietId, String memberId, String mealType, String foodItems, int calories) {
        this.dietId = dietId;
        this.memberId = memberId;
        this.mealType = mealType;
        this.foodItems = foodItems;
        this.calories = calories;
    }

    public String getDietId() { return dietId; }
    public void setDietId(String dietId) { this.dietId = dietId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getFoodItems() { return foodItems; }
    public void setFoodItems(String foodItems) { this.foodItems = foodItems; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public String toCsvLine() {
        return String.join(",",
                safe(dietId),
                safe(memberId),
                safe(mealType),
                safe(foodItems),
                String.valueOf(calories)
        );
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
