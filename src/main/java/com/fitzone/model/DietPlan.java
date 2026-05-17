package com.fitzone.model;

public class DietPlan {
    private String dietId;
    private String memberId;
    private String mealType;
    private String foodItems;
    private int calories;
    private String imageUrl;

    public DietPlan() {}

    public DietPlan(String dietId, String memberId, String mealType, String foodItems, int calories, String imageUrl) {
        this.dietId = dietId;
        this.memberId = memberId;
        this.mealType = mealType;
        this.foodItems = foodItems;
        this.calories = calories;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /**
     * Converts a filesystem path like 'main\resources\static\images\photo' 
     * into a web-accessible URL like '/images/photo.jpg'.
     */
    public String getDisplayImageUrl() {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?q=80&w=1000";
        }
        
        if (imageUrl.startsWith("http")) {
            return imageUrl;
        }

        // Remove project structure prefixes if present
        String path = imageUrl.replace("src\\main\\resources\\static", "")
                             .replace("src/main/resources/static", "")
                             .replace("main\\resources\\static", "")
                             .replace("main/resources/static", "");
        
        // Normalize slashes
        path = path.replace("\\", "/");
        
        // Ensure it starts with /
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        return path;
    }

    public String toCsvLine() {
        return String.join(",",
                safe(dietId),
                safe(memberId),
                safe(mealType),
                safe(foodItems),
                String.valueOf(calories),
                safe(imageUrl)
        );
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
