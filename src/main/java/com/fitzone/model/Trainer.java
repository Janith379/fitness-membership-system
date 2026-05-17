package com.fitzone.model;

public class Trainer {
    private String trainerId;
    private String fullName;
    private String specialization;
    private String phone;
    private String email;
    private String imageUrl;
    private String status; // Available, Busy, On Leave

    public Trainer() {
        this.status = "Available";
    }

    public Trainer(String trainerId, String fullName, String specialization, String phone, String email, String imageUrl, String status) {
        this.trainerId = trainerId;
        this.fullName = fullName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.imageUrl = imageUrl;
        this.status = status != null ? status : "Available";
    }

    public Trainer(String trainerId, String fullName, String specialization, String phone, String email, String imageUrl) {
        this(trainerId, fullName, specialization, phone, email, imageUrl, "Available");
    }

    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /**
     * Converts a filesystem path like 'main\resources\static\images\photo' 
     * into a web-accessible URL like '/images/photo.jpg'.
     */
    public String getDisplayImageUrl() {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return "/images/trainer_2.png";
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
                safe(trainerId),
                safe(fullName),
                safe(specialization),
                safe(phone),
                safe(email),
                safe(imageUrl),
                safe(status)
        );
    }

    private String safe(String value) {
        return value != null ? value : "";
    }
}
